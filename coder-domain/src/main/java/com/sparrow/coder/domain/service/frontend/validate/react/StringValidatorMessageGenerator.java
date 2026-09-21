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
