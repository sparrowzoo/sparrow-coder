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

package com.sparrow.coder.domain.service.frontend.architecture;

import com.sparrow.coder.constant.ArchitectureNames;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractArchitectureGenerator;
import com.sparrow.coder.domain.service.frontend.FrontendGenerator;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.ArchitectureCategory;
import com.sparrow.coder.enums.FrontendKey;
import jakarta.inject.Named;

import java.io.IOException;

@Named
public class ReactArchitectureGenerator extends AbstractArchitectureGenerator {
    @Override
    public void generate(TableConfigRegistry registry, String tableName) throws IOException {
        TableContext tableContext = registry.getTableContext(tableName);
        FrontendGenerator frontendGenerator = tableContext.getFrontendGenerator();
        frontendGenerator.generate(FrontendKey.PAGE, registry);
        frontendGenerator.generate(FrontendKey.API, registry);
        frontendGenerator.generate(FrontendKey.ADD, registry);
        frontendGenerator.generate(FrontendKey.EDIT, registry);
        frontendGenerator.generate(FrontendKey.SEARCH, registry);
        frontendGenerator.generate(FrontendKey.OPERATION, registry);
        frontendGenerator.generate(FrontendKey.SCHEMA, registry);
        frontendGenerator.generate(FrontendKey.COLUMNS, registry);
        frontendGenerator.generate(FrontendKey.MESSAGE, registry);
        frontendGenerator.generate(FrontendKey.MESSAGE_FILE_LIST, registry);
    }

    @Override
    public ArchitectureCategory getCategory() {
        return ArchitectureCategory.FRONTEND;
    }

    @Override
    public String getName() {
        return ArchitectureNames.REACT;
    }
}
