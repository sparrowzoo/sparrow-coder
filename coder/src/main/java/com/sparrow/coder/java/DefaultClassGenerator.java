package com.sparrow.coder.java;

import com.sparrow.coder.config.CoderConfig;
import com.sparrow.coder.java.enums.PlaceholderKey;
import com.sparrow.coder.support.utils.ConfigUtils;
import com.sparrow.coding.api.TableConfigRegistry;
import com.sparrow.coding.api.backend.ClassGenerator;
import com.sparrow.coding.enums.ClassKey;
import com.sparrow.coding.po.TableConfig;
import com.sparrow.coding.protocol.TableContext;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
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
    private Properties config;
    private FileUtility fileUtility = FileUtility.getInstance();

    public DefaultClassGenerator(TableConfigRegistry registry) throws IOException {
        this.registry = registry;
        this.config = ConfigUtils.initPropertyConfig(registry.getProjectConfig().getConfig());
    }

    @Override
    public String getPackage(String tableName, ClassKey classKey) {
        String packageName = this.config.getProperty(CoderConfig.PACKAGE_PREFIX + classKey.name().toLowerCase());
        String poPackage = this.registry.getTableContext(tableName).getPoPackage();
        return fileUtility.replacePath(poPackage, ClassKey.PO.name(), packageName, Symbol.DOT);
    }


    public String getClassName(String tableName, ClassKey classKey) {
        String persistenceClassName = this.registry.getTableContext(tableName).getPlaceHolder().get(PlaceholderKey.$persistence_class_name.name());
        String source = config.getProperty(CoderConfig.CLASS_PREFIX + classKey.name().toLowerCase());
        if (persistenceClassName == null) {
            return source;
        }
        return source.replace(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
    }

    public String getModule(String tableName, ClassKey key) {
        String parentModule = "admin";
        String moduleKey = CoderConfig.MODULE_PREFIX;
        if (!ClassKey.PO.equals(key)) {
            moduleKey += parentModule + "." + key.getModule().toLowerCase();
        } else {
            moduleKey += key.getModule().toLowerCase();
        }
        String modulePath = config.getProperty(moduleKey);
        if (modulePath == null) {
            log.error("module path is null, module key is [{}]", moduleKey);
        }
        Map<String, String> placeHolder = this.registry.getTableContext(tableName).getPlaceHolder();
        return StringUtility.replace(modulePath, placeHolder);
    }


    @Override
    public String getFullPhysicalPath(String tableName, ClassKey classKey) {
        TableConfig tableConfig = registry.getTableContext(tableName).getTableConfig();
        String modulePath = this.getModule(tableName, classKey);
        String path = "";
        if (ClassKey.DAO_MYBATIS.getModule().equals(classKey.getModule())) {
            path = "src" + File.separator
                    + "main" + File.separator
                    + "resources" + File.separator + "mapper";
        } else {
            String fullPackage = this.getPackage(tableName, classKey);
            path = "src" + File.separator
                    + "main" + File.separator
                    + "java" + File.separator
                    + fullPackage.replace('.', File.separatorChar);
        }
        String project = registry.getProjectConfig().getName();
        String projectPath = project + File.separator;
        String parentModulePath = ClassKey.PO.equals(classKey) ? "" : "admin" + File.separator;
        String module = StringUtility.isNullOrEmpty(modulePath) ? "" : modulePath + File.separator;
        String fullPath = registry.getWorkspace() + File.separator +
                tableConfig.getCreateUserName() + File.separator
                + projectPath
                + parentModulePath
                + module + path;
        TableContext tableContext = this.registry.getTableContext(tableName);
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        fullPath = StringUtility.replace(fullPath, placeHolder);
        System.out.println("write to " + fullPath);
        return fullPath;
    }

    public String readConfigContent(String templateFileName) {
        String codeTemplateRoot = registry.getProjectConfig().getCodeTemplate();
        if (!codeTemplateRoot.startsWith("/")) {
            codeTemplateRoot = "/" + codeTemplateRoot;
        }
        String configFilePath = codeTemplateRoot + "/" + templateFileName;
        log.info("config file path is {}\n", configFilePath);
        InputStream inputStream = EnvironmentSupport.getInstance().getFileInputStreamInCache(configFilePath);
        if (inputStream == null) {
            log.error("{} can't read", configFilePath);
        }
        return FileUtility.getInstance().readFileContent(inputStream, StandardCharsets.UTF_8.name());
    }

    @Override
    public void generate(String tableName, ClassKey classKey) throws IOException {
        if (ClassKey.DAO_MYBATIS.equals(classKey)) {

            return;
        }
        String workspace = registry.getWorkspace();
        System.err.printf("current path is [%s]\n", workspace);
        String licensed = FileUtility.getInstance().readFileContent("/Licensed.txt");
        String content;
        if (classKey.equals(ClassKey.PO)) {
            TableContext context = registry.getTableContext(tableName);
            TableConfig tableConfig = context.getTableConfig();
            content = tableConfig.getSourceCode();
        } else {
            content = readConfigContent(classKey.getTemplate());
        }
        Map<String, String> placeHolder = this.registry.getTableContext(tableName).getPlaceHolder();
        content = StringUtility.replace(content.trim(), placeHolder);
        content = licensed + "\n" + content;
        String fullPhysicalPath = this.getFullPhysicalPath(tableName, classKey);
        String extension = ".java";
        String className = getClassName(tableName, classKey);
        fullPhysicalPath += File.separator + className + extension;
        this.fileUtility.writeFile(fullPhysicalPath, content);
    }
}
