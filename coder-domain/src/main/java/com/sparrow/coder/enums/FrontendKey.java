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
import lombok.Getter;

/**
 * 前端组件模板枚举
 */
@Getter
public enum FrontendKey implements EnumIdentityAccessor {
    PAGE("page.txt", 1),
    API("api.txt", 2),
    ADD("add.txt", 3),
    EDIT("edit.txt", 4),
    SEARCH("search.txt", 5),
    OPERATION("operation.txt", 6),
    COLUMNS("columns.txt", 7),
    MESSAGE("", 8),
    MESSAGE_FILE_LIST("", 9),
    SCHEMA("schema.txt", 10);

    private String template;
    private Integer id;

    FrontendKey(String template, Integer id) {
        this.id = id;
        this.template = template;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public static FrontendKey getEnum(Integer id) {
        for (FrontendKey key : FrontendKey.values()) {
            if (key.getIdentity().equals(id)) {
                return key;
            }
        }
        return null;
    }
}
