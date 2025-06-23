package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.FrontendKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DefaultFrontendGenerator implements FrontendGenerator {
    private final ProjectConfigBO projectConfig;
    private TableConfigRegistry registry;
    private FrontendPlaceholder frontendArchAccessor;

    private Map<String, String> i18n = new HashMap<>();

    private String template;

    private TableContext tableContext;

    public DefaultFrontendGenerator(TableConfigRegistry registry, String tableName, String template) {
        this.template = template;
        this.registry = registry;
        this.projectConfig = registry.getProject();
        this.frontendArchAccessor = new DefaultFrontendPlaceholder(registry, tableName, template);
        this.tableContext = registry.getTableContext(tableName);
    }

    @Override
    public String getFullPhysicalPath(FrontendKey key) {
        String project = this.projectConfig.getFrontendName();
        String home = registry.getEnvConfig().getHome(this.projectConfig.getCreateUserId());
        return new FileNameBuilder(registry.getEnvConfig().getWorkspace())
                .joint(registry.getEnvConfig().getFrontendProjectRoot())
                .joint(home)
                .joint(project)
                .joint(this.frontendArchAccessor.getPath(key))
                .build();
    }

    @Override
    public String readConfigContent(String templateFileName) {
        String codeTemplateRoot = this.template;
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
    public void generate(FrontendKey key) throws IOException {
        String content = null;
        if (key.equals(FrontendKey.MESSAGE)) {
            content = toi18nMessage();
        } else {
            content= readConfigContent(key.getTemplate());
            Map<String, String> placeHolder = tableContext.getPlaceHolder();
            content = StringUtility.replace(content.trim(), placeHolder);
        }
        String fullPhysicalPath = this.getFullPhysicalPath(key);
        log.info("generate file name is [{}]", fullPhysicalPath);
        FileUtility.getInstance().writeFile(fullPhysicalPath, content);
    }

    private String toi18nMessage() {
        return JsonFactory.getProvider().toString(tableContext.getI18nMap());
    }
}
