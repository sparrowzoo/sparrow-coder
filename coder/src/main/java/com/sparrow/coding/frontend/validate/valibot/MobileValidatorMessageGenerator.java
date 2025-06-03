package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.MobileValidator;

import javax.inject.Named;

@Named
public class MobileValidatorMessageGenerator extends AbstractValidatorMessageGenerator<MobileValidator> {
    @Override
    public String generateValidateMessage(String propertyName, MobileValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.start(propertyName));
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.minLength(validator));
        pipeline.append(this.maxLength(validator));
        pipeline.append(String.format("v.regex(%1$s, \"%2$s\"))])","1\\d{10}",this.getMessage(validator.getI18n(),"format-message",validator.getFormatMessage())));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
