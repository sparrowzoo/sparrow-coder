package com.sparrowzoo.coder.domain.service.backend;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ClassKey;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class DefaultClassGenerator implements ClassGenerator {
    private final String architecture;
    private final ProjectConfigBO projectConfig;

    private TableContext tableContext;
    private TableConfigRegistry registry;

    protected final FileUtility fileUtility = FileUtility.getInstance();

    private ClassPlaceholder classPlaceholder;

    public DefaultClassGenerator(TableConfigRegistry registry, String tableName, String architecture) throws IOException {
        this.registry = registry;
        this.tableContext = registry.getTableContext(tableName);
        this.projectConfig = registry.getProject();
        this.architecture = architecture;
        this.classPlaceholder=new DefaultClassPlaceholder(registry,tableName);
    }


    @Override
    public String getFullPhysicalPath(ClassKey classKey) {
        String modulePath =this.classPlaceholder.getModule(classKey);
        String path = "";
        if (ClassKey.DAO_MYBATIS.getModule().equals(classKey.getModule())) {
            path = new FileNameBuilder("src")
                    .joint("main")
                    .joint("resources")
                    .joint("mapper")
                    .build();
        } else {
            String fullPackage =classPlaceholder.getPackage(classKey);
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
        String home=registry.getEnvConfig().getHome(this.projectConfig.getCreateUserId());
        String className = this.classPlaceholder.getClassName(classKey);
        String extension = ".java";
        String fullPhysicalPath = new FileNameBuilder(registry.getEnvConfig().getWorkspace())
                .joint(registry.getEnvConfig().getProjectRoot())
                .joint(home)
                .joint(project)
                .joint(parentModulePath)
                .joint(module)
                .joint(path)
                .fileName(className)
                .extension(extension)
                .build();
        fullPhysicalPath = StringUtility.replace(fullPhysicalPath, tableContext.getPlaceHolder());
        if (!this.projectConfig.getWrapWithParent()) {
            fullPhysicalPath = fullPhysicalPath.replace("-admin", "");
        }
        return fullPhysicalPath;
    }

    public String readConfigContent(String templateFileName) {
        String codeTemplateRoot =this.architecture;
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
        log.info("generate file name is [{}]", fullPhysicalPath);
        this.fileUtility.writeFile(fullPhysicalPath, content);
    }
}
