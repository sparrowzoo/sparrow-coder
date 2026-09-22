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

package com.sparrow.coder.domain.bo.validate;

import lombok.Data;

@Data
public class RegexValidator extends StringValidator implements Validator {
    public static final RegexValidator REGEX_VALIDATOR = new RegexValidator();
    static {
        StringValidator stringValidator = StringValidator.STRING_VALIDATOR.create();
        REGEX_VALIDATOR.setI18n(stringValidator.i18n);
        REGEX_VALIDATOR.setEmptyMessage(stringValidator.emptyMessage);
        REGEX_VALIDATOR.setAllowEmpty(stringValidator.allowEmpty);
        REGEX_VALIDATOR.setMinLength(stringValidator.minLength);
        REGEX_VALIDATOR.setMaxLength(stringValidator.maxLength);
        REGEX_VALIDATOR.setMinLengthMessage(stringValidator.minLengthMessage);
        REGEX_VALIDATOR.setMaxLengthMessage(stringValidator.maxLengthMessage);
        REGEX_VALIDATOR.setI18nConfig(stringValidator.i18nConfig);
        REGEX_VALIDATOR.setFormatMessage("请输入正确格式的：%s");
    }
    private String formatMessage;
    private String regex;

    public RegexValidator create() {
        RegexValidator validator = new RegexValidator();
        validator.setFormatMessage(REGEX_VALIDATOR.formatMessage);
        validator.setRegex(REGEX_VALIDATOR.regex);
        validator.setI18n(REGEX_VALIDATOR.i18n);
        validator.setEmptyMessage(REGEX_VALIDATOR.emptyMessage);
        validator.setAllowEmpty(REGEX_VALIDATOR.allowEmpty);
        validator.setMinLength(REGEX_VALIDATOR.minLength);
        validator.setMaxLength(REGEX_VALIDATOR.maxLength);
        validator.setMinLengthMessage(REGEX_VALIDATOR.minLengthMessage);
        validator.setMaxLengthMessage(REGEX_VALIDATOR.maxLengthMessage);
        validator.setI18nConfig(REGEX_VALIDATOR.i18nConfig);
        return validator;
    }
}
