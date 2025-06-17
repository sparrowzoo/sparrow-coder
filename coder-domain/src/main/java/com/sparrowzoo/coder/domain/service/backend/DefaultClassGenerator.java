package com.sparrowzoo.coder.domain.service.backend;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.service.registry.ArchitectureRegistry;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ClassKey;
import com.sparrowzoo.coder.po.ProjectConfig;
import com.sparrowzoo.coder.po.TableConfig;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.constant.Config;
import com.sparrowzoo.coder.enums.PlaceholderKey;
import com.sparrowzoo.coder.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class DefaultClassGenerator implements ClassGenerator {
    private TableConfigRegistry registry;

    private ProjectConfigBO projectConfig;
    private TableContext tableContext;
    private Properties config;
    private FileUtility fileUtility = FileUtility.getInstance();

    public DefaultClassGenerator(TableConfigRegistry registry,String tableName) throws IOException {
        this.registry = registry;
        this.config = ConfigUtils.initPropertyConfig(registry.getProjectConfig().getConfig());
        this.projectConfig=registry.getProjectConfig();
        this.tableContext=registry.getTableContext(tableName);
    }

    @Override
    public String getPackage(ClassKey classKey) {
        String packageName = this.config.getProperty(Config.PACKAGE_PREFIX + classKey.name().toLowerCase());
        if (packageName == null) {
            return "";
        }
        if (!registry.getProjectConfig().getWrapWithParent()) {
            packageName = packageName.replaceAll("admin.", "");
        }
        String poPackage =this.tableContext.getPoPackage();
        return fileUtility.replacePath(poPackage, ClassKey.PO.name(), packageName, Symbol.DOT);
    }


    public String getClassName(ClassKey classKey) {
        String persistenceClassName = this.tableContext.getPlaceHolder().get(PlaceholderKey.$persistence_class_name.name());
        String source = config.getProperty(Config.CLASS_PREFIX + classKey.name().toLowerCase());
        if (persistenceClassName == null) {
            return source;
        }
        return source.replace(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
    }

    public String getModule(ClassKey key) {
        String parentModule = "admin";
        String moduleKey = Config.MODULE_PREFIX;
        if (!ClassKey.PO.equals(key)) {
            moduleKey += parentModule + "." + key.getModule().toLowerCase();
        } else {
            moduleKey += key.getModule().toLowerCase();
        }
        String modulePath = config.getProperty(moduleKey);
        if (modulePath == null) {
            log.error("module path is null, module key is [{}]", moduleKey);
        }
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        return StringUtility.replace(modulePath, placeHolder);
    }


    @Override
    public String getFullPhysicalPath(ClassKey classKey) {
        String modulePath = this.getModule(classKey);
        String path = "";
        if (ClassKey.DAO_MYBATIS.getModule().equals(classKey.getModule())) {
            path = new FileNameBuilder("src")
                    .joint("main")
                    .joint("resources")
                    .joint("mapper")
                    .build();
        } else {
            String fullPackage = this.getPackage(classKey);
            path = new FileNameBuilder("src")
                    .joint("main")
                    .joint("java")
                    .joint(fullPackage.replace('.', File.separatorChar))
                    .build();
        }
        String project = this.projectConfig.getName();
        String parentModulePath = ClassKey.PO.equals(classKey) ? "" :
                this.projectConfig.getWrapWithParent() ? "admin" : "";
        String module = StringUtility.isNullOrEmpty(modulePath) ? "" : modulePath + File.separator;
        String home=this.projectConfig.getImplanted()?"":String.valueOf(this.projectConfig.getCreateUserId());
        String fullPath = new FileNameBuilder(registry.getEnvConfig().getWorkspace())
                .joint(registry.getEnvConfig().getProjectRoot())
                .joint(home)
                .joint(project)
                .joint(parentModulePath)
                .joint(module)
                .joint(path)
                .build();
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        fullPath = StringUtility.replace(fullPath, placeHolder);
        return fullPath;
    }

    public String readConfigContent(String templateFileName) {
         ArchitectureRegistry.getInstance().getGenerator();

        String codeTemplateRoot =this.projectConfig.getArchitectures();
        if (!codeTemplateRoot.startsWith(File.separator)) {
            codeTemplateRoot = File.separator + codeTemplateRoot;
        }
        String configFilePath = codeTemplateRoot + File.separator + templateFileName;
        log.info("config file path is {}\n", configFilePath);
        InputStream inputStream = EnvironmentSupport.getInstance().getFileInputStreamInCache(configFilePath);
        if (inputStream == null) {
            log.error("{} can't read", configFilePath);
        }
        return FileUtility.getInstance().readFileContent(inputStream, StandardCharsets.UTF_8.name());
    }

    @Override
    public void generate(ClassKey classKey) throws IOException {
        if (ClassKey.DAO_MYBATIS.equals(classKey)) {
            return;
        }
        String workspace = registry.getEnvConfig().getWorkspace();
        System.err.printf("current path is [%s]\n", workspace);
        String licensed = FileUtility.getInstance().readFileContent(File.separator+"Licensed.txt");
        String content;
        if (classKey.equals(ClassKey.PO)) {
            TableConfigBO tableConfig = tableContext.getTableConfig();
            content = tableConfig.getSourceCode();
        } else {
            content = readConfigContent(classKey.getTemplate());
        }
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        content = StringUtility.replace(content.trim(), placeHolder);
        content = licensed + "\n" + content;
        String fullPhysicalPath = this.getFullPhysicalPath(classKey);
        if (!this.projectConfig.getWrapWithParent()) {
            fullPhysicalPath = fullPhysicalPath.replace("-admin", "");
        }
        String className = getClassName(classKey);
        String extension = ".java";
        String fileName = new FileNameBuilder(fullPhysicalPath)
                .fileName(className)
                .extension(extension)
                .build();
        log.info("generate file name is [{}]", fileName);
        this.fileUtility.writeFile(fileName, content);
    }
}
