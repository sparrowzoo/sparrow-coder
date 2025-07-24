package com.sparrowzoo.coder.domain.service.backend;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.ArchitectureGenerator;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.enums.ClassKey;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class DefaultClassGenerator implements ClassGenerator {
    private final ProjectBO project;
    private TableContext tableContext;
    protected final FileUtility fileUtility = FileUtility.getInstance();

    private ClassPlaceholderGenerator classPlaceholder;

    public DefaultClassGenerator(ProjectBO project, TableContext tableContext){
        this.tableContext =tableContext;
        this.project = project;
        this.classPlaceholder=tableContext.getClassPlaceholderGenerator();
    }


    @Override
    public String getClassPhysicalPath(ClassKey classKey) {
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
        ProjectConfigBO projectConfig=this.project.getProjectConfig();
        String projectName = projectConfig.getName();
        String parentModulePath = ClassKey.PO.equals(classKey) ? "" :
                this.project.getProjectConfig().getWrapWithParent() ? "admin" : "";
        String module = StringUtility.isNullOrEmpty(modulePath) ? "" : modulePath + File.separator;
        String home=this.project.getEnvConfig().getHome(this.project.getProjectConfig().getCreateUserId());
        String className = this.classPlaceholder.getClassName(classKey);
        String extension = ".java";
        String fullPhysicalPath = new FileNameBuilder(this.project.getEnvConfig().getWorkspace())
                .joint(this.project.getEnvConfig().getProjectRoot())
                .joint(home)
                .joint(projectName)
                .joint(parentModulePath)
                .joint(module)
                .joint(path)
                .fileName(className)
                .extension(extension)
                .build();
        fullPhysicalPath = StringUtility.replace(fullPhysicalPath, tableContext.getPlaceHolder());
        if (!this.project.getProjectConfig().getWrapWithParent()) {
            fullPhysicalPath = fullPhysicalPath.replace("-admin", "");
        }
        return fullPhysicalPath;
    }

    public String readTemplateContent(ClassKey classKey) {
        String templateFileName=classKey.getTemplate();
        ArchitectureGenerator architectureGenerator= this.project.getArchitecture(ArchitectureCategory.BACKEND);
        String codeTemplateRoot =architectureGenerator.getName();
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
            content = readTemplateContent(classKey);
        }
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        content = StringUtility.replace(content.trim(), placeHolder);
        content = licensed + "\n" + content;
        String fullPhysicalPath = this.getClassPhysicalPath(classKey);
        File file = new File(fullPhysicalPath);
        if (file.exists()) {
            if(!content.startsWith("overwrite")){
                log.info("file [{}] already exists, skip generate", fullPhysicalPath);
                return;
            }
            content = content.replaceFirst("overwrite", "");
            log.info("file [{}] already exists, overwrite it", fullPhysicalPath);
        }
        log.info("generate file name is [{}]", fullPhysicalPath);
        this.fileUtility.writeFile(fullPhysicalPath, content);
    }
}
