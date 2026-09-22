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

package com.sparrow.coder.domain.service.registry;

import com.sparrow.coder.domain.bo.ProjectBO;
import com.sparrow.coder.domain.bo.TableConfigBO;
import com.sparrow.coder.domain.bo.TableContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考虑上线后多用户访问
 * 为防止内存溢出，这里使用临时变量，每次用户请求量初始化，使用后释放
 */
@Slf4j
@Data
public class TableConfigRegistry {
    public TableConfigRegistry(ProjectBO project) {
        this.project = project;
        this.registry = new HashMap<>();
    }



    private Map<String, TableContext> registry;
    //封装project 维度变量，避免table context 引用registry循环依赖
    private ProjectBO project;

    private PlaceholderExtensionRegistry placeholderExtensionRegistry = PlaceholderExtensionRegistry.getInstance();

    public void register(TableContext tableContext) {
        registry.put(tableContext.getTableConfig().getTableName(), tableContext);
        tableContext.getProject().addI18n(tableContext.getEntityManager().getSimpleClassName());
        this.placeholderExtensionRegistry.extension(tableContext,this);
    }

    public void dependency(TableContext tableContext) {
        this.placeholderExtensionRegistry.dependencyExtension(tableContext,this);
    }
    public TableContext getTableContext(String tableName) {
        return registry.get(tableName);
    }

    public TableContext getFirstTableContext() {
        return registry.values().iterator().next();
    }

    public List<TableConfigBO> getAllTableConfig() {
        List<TableConfigBO> tableConfigList = new ArrayList<>();
        for (String tableName : this.registry.keySet()) {
            tableConfigList.add(this.registry.get(tableName).getTableConfig());
        }
        return tableConfigList;
    }
}
