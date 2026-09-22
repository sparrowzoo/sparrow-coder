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
@EnumUniqueName(name = EnumNames.CELL_TYPE)
public enum CellType implements EnumIdentityAccessor {
    NORMAL("NormalCell", "normal", "标准", 1),
    UNIX_TIMESTAMP("UnixTimestampCell", "unix-timestamp", "时间戮", 6),
    TREE("TreeCell", "tree", "树形", 2),
    OPERATION("OperationCell", "operation", "命令操作", 3),
    CHECK_BOX("CheckBoxCell", "check-box", "选择", 4),
    CURRENCY("CurrencyCell", "currency", "货币", 5);

    private String componentName;
    private String fileName;
    private String description;
    private Integer id;


    CellType(String componentName, String fileName, String description, Integer id) {
        this.componentName = componentName;
        this.fileName = fileName;
        this.description = description;
        this.id = id;
    }

    @Override
    public Integer getIdentity() {
        return id;
    }

    public static CellType getById(Integer id) {
        for (CellType cellType : CellType.values()) {
            if (cellType.getId().equals(id)) {
                return cellType;
            }
        }
        return null;
    }
}
