package com.sparrow.coder.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.StringValidator;

import javax.inject.Named;

@Named
public class StringValidatorMessageGenerator extends AbstractValidatorMessageGenerator<StringValidator> {


    @Override
    public String outerGenerateMessage(String propertyName, StringValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.minLength(validator));
        pipeline.append(this.maxLength(validator));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
