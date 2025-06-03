package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.EmailValidator;

import javax.inject.Named;

@Named
public class EmailValidatorMessageGenerator extends AbstractValidatorMessageGenerator<EmailValidator>{

    @Override
    public String generateValidateMessage(String propertyName, EmailValidator validator){
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.start(propertyName));
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.minLength(validator));
        pipeline.append(this.maxLength(validator));
        pipeline.append(String.format("v.email(%s)",this.getMessage(validator.getI18n(),"email-message",validator.getFormatMessage())));
        this.finish(pipeline);
        if (validator.isAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
