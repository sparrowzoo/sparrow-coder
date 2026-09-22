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

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.json.Json;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrow.coder.domain.bo.ColumnDef;
import com.sparrow.coder.domain.bo.ProjectBO;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.enums.FrontendKey;
import com.sparrow.coder.enums.PlaceholderKey;

import java.io.File;
import java.util.List;

public class DefaultFrontendPlaceholder implements FrontendPlaceholderGenerator {
    protected final ProjectBO project;
    protected final TableContext tableContext;
    protected List<ColumnDef> columnDefs;
    protected Json json = JsonFactory.getProvider();

    public DefaultFrontendPlaceholder(ProjectBO project, TableContext tableContext) {
        this.project = project;
        this.tableContext = tableContext;
        this.columnDefs = tableContext.getColumns();
    }

    @Override
    public String getPath(FrontendKey key) {
        String originPath = this.project.getScaffoldConfig().getProperty(key.name().toLowerCase());
        String persistenceClassName = tableContext.getEntityManager().getSimpleClassName();
        String persistenceObjectByDot = StringUtility.humpToLower(persistenceClassName, '-');
        originPath = originPath.replace(PlaceholderKey.$persistence_object_by_horizontal.name(), persistenceObjectByDot);
        originPath = originPath.replace(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(originPath);
        return new FileNameBuilder(fileNameProperty.getName().replace(".", File.separator)).extension(fileNameProperty.getExtension()).build();
    }
}
