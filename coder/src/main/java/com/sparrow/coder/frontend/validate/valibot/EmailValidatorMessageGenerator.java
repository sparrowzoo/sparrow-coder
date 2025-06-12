package com.sparrow.coder.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.RegexValidator;

import javax.inject.Named;

@Named
public class EmailValidatorMessageGenerator extends AbstractValidatorMessageGenerator<RegexValidator>{

    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator){
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.minLength(validator));
        pipeline.append(this.maxLength(validator));
        pipeline.append(String.format(",\nv.email(%s)",this.getMessage(validator,"email-message",validator.getFormatMessage())));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
