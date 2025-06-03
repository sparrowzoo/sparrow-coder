package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.MobileValidator;
import com.sparrow.coding.protocol.validate.TelValidator;
import javax.inject.Named;

@Named
public class TelValidatorMessageGenerator extends AbstractValidatorMessageGenerator<TelValidator> {

    @Override
    public String generateValidateMessage(String propertyName, TelValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.start(propertyName));
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.minLength(validator));
        pipeline.append(this.maxLength(validator));
        pipeline.append(String.format("v.regex(%1$s, \"%2$s\"))])","(\\d{4}-|\\d{3}-)?(\\d{8}|\\d{7})",this.getMessage(validator.getI18n(),"format-message",validator.getFormatMessage())));
        this.finish(pipeline);
        if (validator.isAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
