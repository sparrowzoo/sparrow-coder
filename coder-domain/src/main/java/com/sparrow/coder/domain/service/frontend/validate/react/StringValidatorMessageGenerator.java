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

import com.sparrow.coder.domain.bo.validate.StringValidator;
import jakarta.inject.Named;

@Named
public class StringValidatorMessageGenerator extends AbstractValidatorMessageGenerator<StringValidator> {

    @Override
    public String outerGenerateMessage(String propertyName, StringValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(propertyName,validator));
        pipeline.append(this.minLength(propertyName,validator));
        pipeline.append(this.maxLength(propertyName,validator));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }

    @Override
    public StringValidator defaultValidator() {
        return StringValidator.STRING_VALIDATOR;
    }
}
