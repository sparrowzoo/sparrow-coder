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
import com.sparrowzoo.coder.domain.service.MybatisEntityManager;
import com.sparrowzoo.coder.domain.service.TemplateEngineer;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.domain.service.template.TemplateEngineerProvider;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.enums.ClassKey;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class DefaultClassGenerator implements ClassGenerator {
    private final ProjectBO project;
    private final TableContext tableContext;
    protected final FileUtility fileUtility = FileUtility.getInstance();

    private final ClassPlaceholderGenerator classPlaceholderGenerator;

    private final TemplateEngineer templateEngineer = TemplateEngineerProvider.getEngineer();

    public DefaultClassGenerator(ProjectBO project, TableContext tableContext) {
        this.tableContext = tableContext;
        this.project = project;
        this.classPlaceholderGenerator = tableContext.getClassPlaceholderGenerator();
    }


    @Override
    public String getClassPhysicalPath(ClassKey classKey) {
        String modulePath = this.classPlaceholderGenerator.getModule(classKey);
        String extension = ".java";
        String path = "";
        if (ClassKey.DAO_MYBATIS.getModule().equals(classKey.getModule())) {
            path = new FileNameBuilder("src").joint("main").joint("resources").joint("mapper").build();
            extension = ".xml";
        } else {
            String fullPackage = classPlaceholderGenerator.getPackage(classKey);
            path = new FileNameBuilder("src").joint("main").joint("java").joint(fullPackage.replace('.', File.separatorChar)).build();
        }
        ProjectConfigBO projectConfig = this.project.getProjectConfig();
        String projectName = projectConfig.getName();
        String parentModulePath = ClassKey.PO.equals(classKey) ? "" : this.project.getProjectConfig().getWrapWithParent() ? "admin" : "";
        String module = StringUtility.isNullOrEmpty(modulePath) ? "" : modulePath + File.separator;
        String home = this.project.getEnvConfig().getHome(this.project.getProjectConfig().getCreateUserId());
        String className = this.classPlaceholderGenerator.getClassName(classKey);
        String fullPhysicalPath = new FileNameBuilder(this.project.getEnvConfig().getWorkspace()).joint(this.project.getEnvConfig().getProjectRoot()).joint(home).joint(projectName).joint(parentModulePath).joint(module).joint(path).fileName(className).extension(extension).build();
        fullPhysicalPath = StringUtility.replace(fullPhysicalPath, tableContext.getPlaceHolder());
        if (!this.project.getProjectConfig().getWrapWithParent()) {
            fullPhysicalPath = fullPhysicalPath.replace("-admin", "");
        }
        return fullPhysicalPath;
    }

    public String readTemplateContent(ClassKey classKey) {
        String templateFileName = classKey.getTemplate();
        ArchitectureGenerator architectureGenerator = this.project.getArchitecture(ArchitectureCategory.BACKEND);
        String codeTemplateRoot = architectureGenerator.getName();
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
    public void generate(ClassKey classKey, TableConfigRegistry registry) throws IOException {
        String licensed = FileUtility.getInstance().readFileContent(File.separator + "Licensed.txt");
        String content;
        if (classKey.equals(ClassKey.PO)) {
            TableConfigBO tableConfig = tableContext.getTableConfig();
            content = tableConfig.getSourceCode();
        } else if (classKey.equals(ClassKey.DAO_MYBATIS)) {
            try {
                MybatisEntityManager mybatisEntityManager = new MybatisEntityManager(Class.forName(tableContext.getTableConfig().getClassName()));
                mybatisEntityManager.init();
                content = mybatisEntityManager.getXml();
            } catch (ClassNotFoundException e) {
                log.error("can't find class {}", tableContext.getTableConfig().getClassName());
                return;
            }
        } else {
            content = readTemplateContent(classKey);
        }
        content = this.templateEngineer.generate(content.trim(), tableContext, registry);
        String fullPhysicalPath = this.getClassPhysicalPath(classKey);
        File file = new File(fullPhysicalPath);
        if (file.exists()) {
            if (!content.startsWith("overwrite")) {
                log.info("file [{}] already exists, skip generate", fullPhysicalPath);
                return;
            }
            log.info("file [{}] already exists, overwrite it", fullPhysicalPath);
        }
        content = content.replaceFirst("overwrite", "");
        if (!classKey.equals(ClassKey.DAO_MYBATIS)) {
            content = licensed + "\n" + content;
        }
        log.info("generate file name is [{}]", fullPhysicalPath);
        this.fileUtility.writeFile(fullPhysicalPath, content);
    }
}
