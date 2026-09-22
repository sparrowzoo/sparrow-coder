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

package com.sparrow.coder.domain.service.frontend.validate.react;

import com.sparrow.coder.domain.bo.validate.NoneValidator;
import jakarta.inject.Named;

@Named
public class NullableValidatorMessageGenerator extends AbstractValidatorMessageGenerator<NoneValidator> {

    //https://www.66zan.cn/regexdso/
    @Override
    public String outerGenerateMessage(String propertyName, NoneValidator validator) {
        if (validator.getClazz().equals(Boolean.class.getName())) {
            return "v.boolean()";
        }
        if (validator.getClazz().equals(String.class.getName())) {
            return "v.string()";
        }
        if (validator.getClazz().equals(Integer.class.getName()) || validator.getClazz().equals(Long.class.getName()) || validator.getClazz().equals(Double.class.getName()) || validator.getClazz().equals(Float.class.getName())) {
            return "v.number()";
        }
        return "v.any()";
    }

    @Override
    public NoneValidator defaultValidator() {
        return new NoneValidator(String.class.getName());
    }
}
