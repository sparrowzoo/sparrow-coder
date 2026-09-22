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
@EnumUniqueName(name = EnumNames.CONTROL_TYPE)
public enum ControlType implements EnumIdentityAccessor {
    INPUT_TEXT("ValidatableInput","validatable-input", "text", 1),
    LABEL("ValidatableInput","validatable-input", "label", 2),
    //    LINK("lnk", "link", 3),
    INPUT_HIDDEN("ValidatableInput","validatable-input", "hidden", 4),
    INPUT_PASSWORD("ValidatableInput","validatable-input", "password", 5),
    TEXT_AREA("ValidatableTextarea","validatable-textarea", "textarea", 6),
    SELECT("ValidatableSelect","validatable-select", "select", 7),
    //    CODE("validatable-editor", "code", 10),
//    EDITOR("validatable-editor", "editor", 11),
    DATE("ValidatableDate","validatable-date", "date", 12),
    DATE_HHMMSS("ValidatableTime","validatable-time", "time", 13),
    CHECK_BOX("ValidatableInput","validatable-input", "checkbox", 14),
//    FILE("validatable-upload", "file", 16),
//    IMAGE("validatable-upload", "image", 19)
    ;


    private final String component;
    private final String fileName;
    private final String inputType;
    private Integer id;


    ControlType(String component,String fileName, String inputType, Integer id) {
        this.fileName = fileName;
        this.component = component;
        this.inputType = inputType;
        this.id = id;
    }

    @Override
    public Integer getIdentity() {
        return id;
    }

    public static ControlType getControlType(Integer inputType) {
        for (ControlType controlType : ControlType.values()) {
            if (controlType.id.equals(inputType)) {
                return controlType;
            }
        }
        return null;
    }
}
