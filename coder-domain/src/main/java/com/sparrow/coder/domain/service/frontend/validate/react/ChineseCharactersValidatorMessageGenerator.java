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
public class ChineseCharactersValidatorMessageGenerator extends RegexValidatorMessageGenerator {

    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("/^[\\u4e00-\\u9fa5]+$/");
        if (StringUtility.isNullOrEmpty(validator.getFormatMessage())) {
            validator.setFormatMessage(this.defaultValidator.getFormatMessage());
        }
        return super.outerGenerateMessage(propertyName, validator);
    }

    @Override
    public RegexValidator defaultValidator() {
        RegexValidator validator = RegexValidator.REGEX_VALIDATOR.create();
        validator.setFormatMessage("请输入中文字符");
        return validator;
    }
}
