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

package com.sparrow.coder.enums;

import com.sparrow.coder.constant.EnumNames;
import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import lombok.Getter;

@Getter
@EnumUniqueName(name = EnumNames.HEADER_TYPE)
public enum HeaderType implements EnumIdentityAccessor {
    NORMAL(1, "PlainTextHeader", "plain-text", false, false, "标准", ""),
    NORMAL_SORT(2, "NormalHeader", "normal", true, false, "标准(可排序)", ""),
    NORMAL_FILTER(3, "NormalHeader", "normal", false, false, "标准(可过滤)", ""),
    NORMAL_SORT_FILTER(4, "NormalHeader", "normal", true, true, "标准(可过滤,可排序)", ""),
    CHECK_BOX(5, "CheckboxHeader", "check-box", false, false, "选择", "select"),
    COLUMN_FILTER(6, "ColumnFilter", "column-filter", false, false, "过滤", "filter-column"),
    MENU(9, "PlainTextHeader", "plain-text", false, false, "列菜单", "actions"),
    TREE(10, "PlainTextHeader", "plain-text", false, false, "树型", ""),
    EMPTY(99, "EmptyHeader", "empty", false, false, "空白", "");

    private String componentName;
    private String fileName;
    private Boolean sortable;
    private Boolean filterable;
    private String description;
    private Integer id;
    private String specialId;

    HeaderType(Integer id, String componentName, String fileName, Boolean sortable, Boolean filterable, String description, String specialId) {
        this.componentName = componentName;
        this.fileName = fileName;
        this.description = description;
        this.sortable = sortable;
        this.filterable = filterable;
        this.id = id;
        this.specialId = specialId;
    }


    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public static HeaderType getById(Integer id) {
        for (HeaderType headerType : HeaderType.values()) {
            if (headerType.getId().equals(id)) {
                return headerType;
            }
        }
        return null;
    }

    public static Boolean isFilter(HeaderType headerType) {
        return HeaderType.NORMAL_SORT.equals(headerType) ||
                HeaderType.NORMAL_FILTER.equals(headerType) ||
                HeaderType.NORMAL_SORT_FILTER.equals(headerType);
    }

    public static Boolean isFilter(Integer id) {
        return isFilter(getById(id));
    }
}
