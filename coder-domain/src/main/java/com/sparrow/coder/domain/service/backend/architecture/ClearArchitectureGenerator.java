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

import com.sparrow.coder.constant.ArchitectureNames;
import com.sparrow.coder.domain.bo.TableConfigBO;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractArchitectureGenerator;
import com.sparrow.coder.domain.service.backend.ClassGenerator;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.ArchitectureCategory;
import com.sparrow.coder.enums.ClassKey;
import com.sparrow.coder.enums.CodeSource;
import jakarta.inject.Named;

import java.io.IOException;

@Named
public class ClearArchitectureGenerator extends AbstractArchitectureGenerator {

    @Override
    public void generate(TableConfigRegistry registry, String tableName) throws IOException {
        TableContext context = registry.getTableContext(tableName);
        ClassGenerator classGenerator = context.getClassGenerator();
        TableConfigBO tableConfig = context.getTableConfig();
        if (CodeSource.SOURCE_CODE.getIdentity().equals(tableConfig.getSource())) {
            classGenerator.generate(ClassKey.PO,registry);
        }
        classGenerator.generate(ClassKey.BO,registry);
        classGenerator.generate(ClassKey.QUERY,registry);
        classGenerator.generate(ClassKey.PARAM,registry);
        classGenerator.generate(ClassKey.DTO,registry);
        classGenerator.generate(ClassKey.DAO,registry);
        classGenerator.generate(ClassKey.DAO_IMPL,registry);
        classGenerator.generate(ClassKey.DAO_MYBATIS,registry);
        classGenerator.generate(ClassKey.DATA_CONVERTER,registry);
        classGenerator.generate(ClassKey.SERVICE,registry);
        classGenerator.generate(ClassKey.REPOSITORY,registry);
        classGenerator.generate(ClassKey.REPOSITORY_IMPL,registry);
        classGenerator.generate(ClassKey.ASSEMBLE,registry);
        classGenerator.generate(ClassKey.CONTROLLER,registry);
        classGenerator.generate(ClassKey.PAGER_QUERY,registry);
    }

    @Override
    public ArchitectureCategory getCategory() {
        return ArchitectureCategory.BACKEND;
    }

    @Override
    public String getName() {
        return ArchitectureNames.CLEAR;
    }
}
