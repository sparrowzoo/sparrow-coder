/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.coder.domain.service.frontend;

import com.sparrow.coder.domain.bo.ProjectBO;
import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.ArchitectureGenerator;
import com.sparrow.coder.domain.service.EnvConfig;
import com.sparrow.coder.domain.service.TemplateEngineer;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.domain.service.template.TemplateEngineerProvider;
import com.sparrow.coder.enums.ArchitectureCategory;
import com.sparrow.coder.enums.FrontendKey;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.json.Json;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

@Slf4j
public class DefaultFrontendGenerator implements FrontendGenerator {
    private FrontendPlaceholderGenerator frontendArchAccessor;
    private TableContext tableContext;
    private ProjectBO project;
    private String architectureName;
    private Json json = JsonFactory.getProvider();

    private TemplateEngineer templateEngineer = TemplateEngineerProvider.getEngineer();

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
        EnvConfig envConfig = this.project.getEnvConfig();
        String scaffoldFrontendHome = envConfig.getScaffoldFrontendHome();
        if (StringUtility.isNullOrEmpty(scaffoldFrontendHome)) {
            return new FileNameBuilder(envConfig.getProjectHome(projectConfig)).joint(projectConfig.getFrontendName()).joint(this.frontendArchAccessor.getPath(key)).build();
        }
        return new FileNameBuilder(scaffoldFrontendHome).joint(projectConfig.getFrontendName()).joint(this.frontendArchAccessor.getPath(key)).build();
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
    public void generate(FrontendKey key, TableConfigRegistry registry) throws IOException {
        String content = null;
        if (key.equals(FrontendKey.MESSAGE)) {
            content = toi18nMessage();
        } else if (key.equals(FrontendKey.MESSAGE_FILE_LIST)) {
            content = toi18nMessageFileList();
        } else {
            content = readTemplateContent(key);
            content = this.templateEngineer.generate(content.trim(), tableContext, registry);
        }
        String fullPhysicalPath = this.getTargetPhysicalPath(key);
        File file = new File(fullPhysicalPath);
        if (file.exists()) {
            log.info("file [{}] already exists, overwrite it", fullPhysicalPath);
        }
        log.info("generate file name is [{}]", fullPhysicalPath);
        if (project.getEnvConfig().overwrite()) {
            FileUtility.getInstance().writeFile(fullPhysicalPath, content);
        } else {
            log.info("全局 overwrite is false {}", fullPhysicalPath);
        }
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
        if (oldFileList != null) {
            newFileList.addAll(oldFileList);
        }
        newFileList.addAll(this.project.getI18nList());
        return json.toString(newFileList);
    }
}
