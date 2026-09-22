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

package com.sparrow.coder.protocol.enums;

import com.sparrow.protocol.ErrorSupport;
import com.sparrow.protocol.ModuleSupport;
import lombok.Getter;

@Getter
public enum CoderError implements ErrorSupport {
    CLASS_CAN_NOT_ASSIGNABLE_PO(false, CoderModule.CODER, "05", "po class must assignable from  PO"),
    CLASS_NOT_CONTAINS_PO(false, CoderModule.CODER, "04", "po class name must contains '.po'"),
    NOT_SELF_PROJECT(false, CoderModule.CODER, "03", "can't generate other's project"),
    SYSTEM_TABLE(false, CoderModule.CODER, "02", "system table can't be modified"),
    CLASS_NOT_FOUND(false, CoderModule.CODER, "01", "class not found");

    private boolean system;
    private ModuleSupport module;
    private String code;
    private String message;

    CoderError(boolean system, ModuleSupport module, String code, String message) {
        this.system = system;
        this.message = message;
        this.module = module;
        this.code = (system ? 0 : 1) + module.code() + code;
    }

    @Override
    public boolean system() {
        return this.system;
    }

    @Override
    public ModuleSupport module() {
        return this.module;
    }
}
