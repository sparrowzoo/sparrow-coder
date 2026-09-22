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

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrow.coder.constant.EnumNames;
import lombok.Getter;

@Getter
@EnumUniqueName(name = EnumNames.HEADER_TYPE)
public enum HeaderType implements EnumIdentityAccessor {
    NORMAL("PlainTextHeader", "plain-text", false, false, "标准", 1),
    NORMAL_SORT("NormalHeader", "normal", true, false, "标准(可排序)", 2),
    NORMAL_FILTER("NormalHeader", "normal", false, false, "标准(可过滤)", 3),
    NORMAL_SORT_FILTER("NormalHeader", "normal", true, true, "标准(可过滤,可排序)", 4),
    CHECK_BOX("CheckboxHeader", "check-box", false, false, "选择", 5),
    COLUMN_FILTER("ColumnFilter", "column-filter", false, false, "过滤", 6),
    PLAIN_TEXT("PlainTextHeader", "plain-text", false, false, "操作", 9),
    EMPTY("EmptyHeader", "empty", false, false, "空白", 10);
    private String componentName;
    private String fileName;
    private Boolean sortable;
    private Boolean filterable;
    private String description;
    private Integer id;

    HeaderType(String componentName, String fileName, Boolean sortable, Boolean filterable, String description, Integer id) {
        this.componentName = componentName;
        this.fileName = fileName;
        this.description = description;
        this.sortable = sortable;
        this.filterable = filterable;
        this.id = id;
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
}
