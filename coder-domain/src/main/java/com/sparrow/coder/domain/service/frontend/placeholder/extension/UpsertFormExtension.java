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

package com.sparrow.coder.domain.service.frontend.placeholder.extension;

import com.sparrow.utility.CollectionsUtility;
import com.sparrow.coder.domain.bo.ColumnDef;
import com.sparrow.coder.domain.bo.ProjectBO;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.ArchitectureGenerator;
import com.sparrow.coder.domain.service.frontend.generator.column.ColumnGenerator;
import com.sparrow.coder.domain.service.registry.ColumnGeneratorRegistry;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.ArchitectureCategory;
import com.sparrow.coder.enums.PlaceholderKey;
import jakarta.inject.Named;

import java.util.*;

@Named
public class UpsertFormExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        ProjectBO project = tableContext.getProject();
        List<ColumnDef> columnDefs = tableContext.getColumns();
        if (CollectionsUtility.isNullOrEmpty(columnDefs)) {
            return;
        }
        ColumnGeneratorRegistry columnGeneratorRegistry = ColumnGeneratorRegistry.getInstance();
        ArchitectureGenerator architectureGenerator = project.getArchitecture(ArchitectureCategory.FRONTEND);
        String architectureName = architectureGenerator.getName();
        ColumnGenerator columnGenerator = columnGeneratorRegistry.getObject(architectureName);
        this.generatorColumns(tableContext, columnGenerator, project);
    }

    private void generatorColumns(TableContext tableContext, ColumnGenerator columnGenerator, ProjectBO project) {
        List<ColumnDef> columnDefs = tableContext.getColumns();
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        List<String> addFormItems = new ArrayList<>();
        List<String> editFormItems = new ArrayList<>();
        Set<String> imports = new HashSet<>();
        Map<String, Object> columnI18nMap = tableContext.getI18nMap();
        for (ColumnDef columnDef : columnDefs) {
            if (columnDef.getShowInEdit()) {
                imports.add(columnGenerator.importEdit(columnDef, project));
                addFormItems.add(columnGenerator.edit(columnDef, project, true));
                editFormItems.add(columnGenerator.edit(columnDef, project, false));
            }
            columnI18nMap.put(columnDef.getPropertyName(), columnDef.getChineseName());
        }
        String addFormItemStr = String.join("\n", addFormItems);
        String editFormItemStr = String.join("\n", editFormItems);
        placeholder.put(PlaceholderKey.$frontend_edit_import.name(), String.join("\n", imports));
        placeholder.put(PlaceholderKey.$frontend_add_form_items.name(), addFormItemStr);
        placeholder.put(PlaceholderKey.$frontend_edit_form_items.name(), editFormItemStr);
    }
}
