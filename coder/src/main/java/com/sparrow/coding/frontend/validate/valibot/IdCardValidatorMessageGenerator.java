package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.IdCardValidator;
import javax.inject.Named;

@Named
public class IdCardValidatorMessageGenerator extends AbstractValidatorMessageGenerator<IdCardValidator> {

    //https://www.66zan.cn/regexdso/
    @Override
    public String generateValidateMessage(String propertyName, IdCardValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.start(propertyName));
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.minLength(validator));
        pipeline.append(this.maxLength(validator));
        pipeline.append(String.format("v.regex(%1$s, \"%2$s\"))])","\\d{15}(\\d\\d[0-9xX])?",this.getMessage(validator.getI18n(),"format-message",validator.getFormatMessage())));
        this.finish(pipeline);
        if (validator.isAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
