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
import com.sparrow.coder.domain.bo.validate.DigitalValidator;
import jakarta.inject.Named;

@Named
public class DigitalValidatorMessageGenerator extends AbstractValidatorMessageGenerator<DigitalValidator> {
    @Override
    public String outerGenerateMessage(String propertyName, DigitalValidator validator) {
        if (StringUtility.isNullOrEmpty(validator.getDigitalMessage())) {
            validator.setDigitalMessage(this.defaultValidator.getDigitalMessage());
        }
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(propertyName,validator));
        pipeline.append(this.check(propertyName,validator, validator.getCategory().getRegex(), validator.getDigitalMessage()));
        pipeline.append(this.transform(validator.getCategory()));
        pipeline.append(this.minValue(propertyName,validator));
        pipeline.append(this.maxValue(propertyName,validator));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }


    @Override
    public DigitalValidator defaultValidator() {
        return DigitalValidator.DIGITAL_VALIDATOR;
    }
}
