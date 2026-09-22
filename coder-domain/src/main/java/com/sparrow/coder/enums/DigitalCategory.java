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
public enum DigitalCategory implements EnumIdentityAccessor {
    INTEGER("/^\\d+$/", "parseInt(input,10)", 1),
    SIGNED_INTEGER("/^-?\\d+$/", "parseInt(input,10)", 2),
    FLOAT("/^-?\\d+\\.\\d+$/", "parseFloat(input)", 3);
    private final String regex;
    private final String converter;
    private final Integer id;

    DigitalCategory(String regex, String converter, Integer id) {
        this.regex = regex;
        this.converter = converter;
        this.id = id;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }
}
