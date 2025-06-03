package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.StringValidator;

import javax.inject.Named;

@Named
public class StringValidatorMessageGenerator extends AbstractValidatorMessageGenerator<StringValidator> {

    @Override
    public String generateValidateMessage(String propertyName, StringValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.start(propertyName));
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.minLength(validator));
        pipeline.append(this.maxLength(validator));
        this.finish(pipeline);
        if (validator.isAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
