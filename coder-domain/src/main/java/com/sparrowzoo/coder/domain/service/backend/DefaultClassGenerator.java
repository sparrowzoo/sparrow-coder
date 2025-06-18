package com.sparrowzoo.coder.domain.service.backend;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ClassKey;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class DefaultClassGenerator extends DefaultClassArchAccessor implements ClassGenerator {
    private final String template;
    private final ProjectConfigBO projectConfig;
    public DefaultClassGenerator(TableConfigRegistry registry,String tableName,String template) throws IOException {
        super(registry,tableName);
        this.projectConfig = registry.getProject();
        this.template= template;
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
        String codeTemplateRoot =this.template;
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
