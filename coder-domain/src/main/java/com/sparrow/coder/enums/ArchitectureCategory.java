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

@Getter
public enum ArchitectureCategory implements EnumIdentityAccessor {
    BACKEND(1, "后端","clear"),
    FRONTEND(2, "前端","react"),
    MOBILE(3, "移动端","mobile"),
    DESKTOP(4, "桌面","pc"),
    MINI_PROGRAM(5, "小程序","mini-program"),
    DATABASE(6, "数据库","mysql");

    private final String name;
    private final Integer id;
    private final String defaultArch;

    ArchitectureCategory(Integer id, String name,String defaultArch) {
        this.id = id;
        this.name = name;
        this.defaultArch = defaultArch;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public static ArchitectureCategory getById(Integer id) {
        for (ArchitectureCategory category : values()) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }
}
