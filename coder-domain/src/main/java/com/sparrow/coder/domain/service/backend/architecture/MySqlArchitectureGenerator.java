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

package com.sparrow.coder.domain.service.backend.architecture;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.orm.EntityManager;
import com.sparrow.utility.FileUtility;
import com.sparrow.coder.constant.ArchitectureNames;
import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.domain.service.AbstractArchitectureGenerator;
import com.sparrow.coder.domain.service.EnvConfig;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.ArchitectureCategory;
import jakarta.inject.Named;

import java.io.IOException;

@Named
public class MySqlArchitectureGenerator extends AbstractArchitectureGenerator {
    @Override
    public void generate(TableConfigRegistry registry, String tableName) throws IOException {
        EntityManager entityManager = registry.getTableContext(tableName).getEntityManager();
        EnvConfig envConfig = registry.getProject().getEnvConfig();
        ProjectConfigBO projectConfig = registry.getProject().getProjectConfig();
        String home = envConfig.getHome(projectConfig.getCreateUserId());
        String fullPath = new FileNameBuilder(envConfig.getWorkspace())
                .joint(envConfig.getProjectRoot())
                .joint(home)
                .joint(projectConfig.getName())
                .joint("ddl")
                .fileName(tableName)
                .extension("sql")
                .build();
        String sql = entityManager.getCreateDDL();
        System.err.println(sql);
        FileUtility.getInstance().writeFile(fullPath, sql);
        System.err.printf("table create ddl write to %s\n", fullPath);
    }

    @Override
    public ArchitectureCategory getCategory() {
        return ArchitectureCategory.DATABASE;
    }

    @Override
    public String getName() {
        return ArchitectureNames.MYSQL;
    }
}
