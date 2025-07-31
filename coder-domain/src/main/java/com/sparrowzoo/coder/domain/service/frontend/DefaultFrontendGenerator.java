package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.json.Json;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.ArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.EnvConfig;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.enums.FrontendKey;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DefaultFrontendGenerator implements FrontendGenerator {
    private FrontendPlaceholderGenerator frontendArchAccessor;
    private TableContext tableContext;
    private ProjectBO project;
    private String architectureName;
    private Json json = JsonFactory.getProvider();

    public DefaultFrontendGenerator(ProjectBO project, TableContext tableContext) {
        this.project = project;
        this.tableContext = tableContext;
        this.frontendArchAccessor = tableContext.getFrontendPlaceholderGenerator();
        ArchitectureGenerator architectureGenerator = project.getArchitecture(ArchitectureCategory.FRONTEND);
        this.architectureName = architectureGenerator.getName();
    }

    @Override
    public String getTargetPhysicalPath(FrontendKey key) {
        ProjectConfigBO projectConfig = this.project.getProjectConfig();
        EnvConfig envConfig = project.getEnvConfig();
        String projectName = projectConfig.getFrontendName();
        String home = envConfig.getHome(projectConfig.getCreateUserId());
        return new FileNameBuilder(envConfig.getWorkspace())
                .joint(envConfig.getFrontendProjectRoot())
                .joint(home)
                .joint(projectName)
                .joint(this.frontendArchAccessor.getPath(key))
                .build();
    }

    @Override
    public String readTemplateContent(FrontendKey frontendKey) {
        String templateFileName = frontendKey.getTemplate();
        String codeTemplateRoot = this.architectureName;
        if (!codeTemplateRoot.startsWith(File.separator)) {
            codeTemplateRoot = File.separator + codeTemplateRoot;
        }
        String configFilePath = codeTemplateRoot + File.separator + templateFileName;
        return FileUtility.getInstance().readFileContent(configFilePath);
    }

    @Override
    public void generate(FrontendKey key) throws IOException {
        String content = null;
        boolean allSkip = false;
        if (key.equals(FrontendKey.MESSAGE)) {
            content = toi18nMessage();
        } else if (key.equals(FrontendKey.MESSAGE_FILE_LIST)) {
            content = toi18nMessageFileList();
        } else {
            allSkip = true;
            content = readTemplateContent(key);
            Map<String, String> placeHolder = tableContext.getPlaceHolder();
            content = StringUtility.replace(content.trim(), placeHolder);
        }
        String fullPhysicalPath = this.getTargetPhysicalPath(key);
        File file = new File(fullPhysicalPath);
        if (file.exists()) {
            if (allSkip&&!content.startsWith("overwrite")) {
                log.info("file [{}] already exists, skip generate", fullPhysicalPath);
                return;
            }
            content = content.replaceFirst("overwrite", "");
            log.info("file [{}] already exists, overwrite it", fullPhysicalPath);
        }
        log.info("generate file name is [{}]", fullPhysicalPath);
        FileUtility.getInstance().writeFile(fullPhysicalPath, content);
    }

    private String toi18nMessage() {
        return json.toString(tableContext.getI18nMap());
    }

    private String toi18nMessageFileList() {
        String messageFileListPath = this.getTargetPhysicalPath(FrontendKey.MESSAGE_FILE_LIST);
        String messageFileList = "";
        if (new File(messageFileListPath).exists()) {
            messageFileList = FileUtility.getInstance().readFileContent(messageFileListPath);
        }
        Set<String> newFileList = new LinkedHashSet<>();
        newFileList.add("default");//default 必须排第一,否则国际化会有问题
        Set<String> oldFileList = json.parse(messageFileList, Set.class);
        if(oldFileList != null) {
            newFileList.addAll(oldFileList);
        }
        newFileList.addAll(this.project.getI18nList());
        return json.toString(newFileList);
    }
}
