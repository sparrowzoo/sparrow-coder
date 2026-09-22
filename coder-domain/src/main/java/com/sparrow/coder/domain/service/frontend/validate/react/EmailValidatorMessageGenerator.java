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

import com.sparrow.utility.StringUtility;
import com.sparrow.coder.domain.bo.validate.RegexValidator;
import jakarta.inject.Named;

@Named
public class EmailValidatorMessageGenerator extends AbstractValidatorMessageGenerator<RegexValidator> {

    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(propertyName,validator));
        pipeline.append(this.minLength(propertyName,validator));
        pipeline.append(this.maxLength(propertyName,validator));
        pipeline.append(this.email(propertyName,validator));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }

    private String email(String propertyName, RegexValidator validator) {
        String message = validator.getFormatMessage();
        if (StringUtility.isNullOrEmpty(message)) {
            message = this.defaultValidator.getFormatMessage();
        }
        return String.format(",\nv.email(%s)", this.getMessage(propertyName,validator, "email-message", message));
    }

    @Override
    public RegexValidator defaultValidator() {
        RegexValidator validator = RegexValidator.REGEX_VALIDATOR.create();
        validator.setFormatMessage("请输入有效的邮箱地址");
        return validator;
    }
}
