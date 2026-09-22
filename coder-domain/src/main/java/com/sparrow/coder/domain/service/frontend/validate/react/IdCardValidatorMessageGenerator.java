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
public class IdCardValidatorMessageGenerator extends RegexValidatorMessageGenerator {

    //https://www.66zan.cn/regexdso/
    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("/^(\\d{15}|\\d{17}[\\dXx])$/");
        if (StringUtility.isNullOrEmpty(validator.getFormatMessage())) {
            validator.setFormatMessage("请输入正确的身份证号码");
        }
        return super.outerGenerateMessage(propertyName, validator);
    }

    @Override
    public RegexValidator defaultValidator() {
        RegexValidator validator = RegexValidator.REGEX_VALIDATOR.create();
        validator.setFormatMessage("请输入正确的身份证号码");
        return validator;
    }
}
