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

package com.sparrow.coder.domain.service.frontend.generator.column;

import com.sparrow.coder.domain.bo.ColumnDef;
import com.sparrow.coder.domain.bo.ProjectBO;
import com.sparrow.coder.enums.CellType;
import com.sparrow.coder.enums.HeaderType;

import java.util.List;

public interface ColumnGenerator {
    String column(ColumnDef columnDef,ProjectBO project);

    String importEdit(ColumnDef columnDef,ProjectBO project);

    String edit(ColumnDef columnDef,ProjectBO project,Boolean add);

    String importHeader(HeaderType headerType,ProjectBO project);

    String importCell(CellType cellType,ProjectBO project);

    String getName();

    String columnDefs(String className, List<String> columns);
}
