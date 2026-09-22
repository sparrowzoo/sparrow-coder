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
public class RegexValidatorMessageGenerator extends AbstractValidatorMessageGenerator<RegexValidator> {

    //https://www.66zan.cn/regexdso/
    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(propertyName,validator));
        if (StringUtility.isNullOrEmpty(validator.getFormatMessage())) {
            validator.setFormatMessage(this.defaultValidator.getFormatMessage() + validator.getRegex());
        }
        pipeline.append(this.check(propertyName,validator, validator.getRegex(), validator.getFormatMessage()));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }

    @Override
    public RegexValidator defaultValidator() {
        return RegexValidator.REGEX_VALIDATOR;
    }
}
