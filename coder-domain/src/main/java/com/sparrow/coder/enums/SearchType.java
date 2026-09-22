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

@EnumUniqueName(name = EnumNames.SEARCH_TYPE)
@Getter
public enum SearchType implements EnumIdentityAccessor {
    EQUAL(1,"equal"),
    PREFIX_LIKE(2,"startWith"),
    SUFFIX_LIKE(3,"endWith"),
    LIKE(4,"contains"),
    DATE_RANGE(5,""),
    LESS(6,"lessThan"),
    LESS_EQUAL(7,"lessThanEqual"),
    GREATER(8,"greaterThan"),
    GREATER_EQUAL(9,"greaterThanEqual"),
    BETWEEN(10,"between"),
    NOT_EQUAL(11,"notEqual");

    private Integer id;
    private String condition;

    SearchType(Integer id,String condition) {
        this.id = id;
        this.condition = condition;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public static SearchType getById(Integer id) {
        for (SearchType type : SearchType.values()) {
            if (type.getIdentity().equals(id)) {
                return type;
            }
        }
        return null;
    }
}
