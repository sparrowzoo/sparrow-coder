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

package com.sparrow.coder.domain.service;

import com.sparrow.coder.domain.bo.*;
import com.sparrow.context.SessionContext;
import com.sparrow.exception.Asserts;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.FileUtility;
import com.sparrow.coder.domain.CoderDomainRegistry;
import com.sparrow.coder.domain.service.backend.ScaffoldCopier;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.protocol.enums.CoderError;
import com.sparrow.coder.protocol.query.TableConfigQuery;
import com.sparrow.coder.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


@Slf4j
public class DefaultCodeGenerator implements CodeGenerator {
    private TableConfigRegistry registry;
    private CoderDomainRegistry domainRegistry;

    public DefaultCodeGenerator(Long projectId, EnvConfig envConfig, CoderDomainRegistry domainRegistry) throws IOException {
        this.domainRegistry = domainRegistry;
        ProjectConfigBO projectConfig = domainRegistry.getProjectConfigRepository().getProjectConfig(projectId);
        Properties config = ConfigUtils.initPropertyConfig(projectConfig.getConfig());
        ProjectBO project = new ProjectBO(projectConfig, config, envConfig);
        this.registry = new TableConfigRegistry(project);
        this.initRegistry();
    }

    public void initRegistry() {
        TableConfigQuery tableConfigQuery = new TableConfigQuery();
        tableConfigQuery.setProjectId(registry.getProject().getProjectConfig().getId());
        tableConfigQuery.setStatus(StatusRecord.ENABLE.getIdentity());
        List<TableConfigBO> tableConfigs = domainRegistry.getTableConfigRepository().queryTableConfigs(tableConfigQuery);

        for (TableConfigBO tableConfigBO : tableConfigs) {
            try {
                TableContext context = new TableContext(tableConfigBO, this.registry.getProject());
                this.registry.register(context);
            } catch (Exception e) {
                log.error("table init error table name:{}, full class name {}", tableConfigBO.getTableName()
                        , tableConfigBO.getClassName(), e);
            }
        }

        for (TableConfigBO tableConfig : tableConfigs) {
            try {
                TableContext tableContext = this.registry.getTableContext(tableConfig.getTableName());
                this.registry.dependency(tableContext);
            } catch (Exception e) {
                log.error("table dependency init error table name{}, class name :{}", tableConfig.getTableName(), tableConfig.getClassName(), e);
            }
        }
    }


    @Override
    public void generate(String tableName) throws IOException, BusinessException {
        TableContext context = registry.getTableContext(tableName);
        if (context == null) {
            throw new IllegalArgumentException("table " + tableName + " not found");
        }
        Asserts.isTrue(!context.getProject().getProjectConfig().getCreateUserId().equals(SessionContext.getLoginUser().getUserId()), CoderError.NOT_SELF_PROJECT);
        if (context.getTableConfig().getLocked()) {
            log.info("table {} is locked, skip generate", context.getTableConfig().getTableName());
            //return;
        }
        ProjectArchsBO architectures = registry.getProject().getArchitectures();
        for (String architectureCategory : architectures.getArchs().keySet()) {
            ArchitectureGenerator architectureGenerator = architectures.getArch(architectureCategory);
            if (architectureGenerator != null) {
                architectureGenerator.generate(registry, tableName);
            }
        }
    }

    @Override
    public void initScaffold() {
        ScaffoldCopier.copy(registry);
    }

    @Override
    public void clear() {
        ProjectBO project = registry.getProject();
        EnvConfig envConfig = registry.getProject().getEnvConfig();
        String home = envConfig.getHome(project.getProjectConfig().getCreateUserId());
        String targetDirectoryPath =
                new FileNameBuilder(envConfig.getWorkspace())
                        .joint(envConfig.getProjectRoot())
                        .joint(home)
                        .joint(project.getProjectConfig().getName())
                        .build();
        FileUtility.getInstance().delete(targetDirectoryPath, System.currentTimeMillis());
    }

    @Override
    public TableConfigRegistry getRegistry() {
        return this.registry;
    }


}
