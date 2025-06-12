package com.sparrow.coder.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.RegexValidator;

import javax.inject.Named;

@Named
public class RegexValidatorMessageGenerator extends AbstractValidatorMessageGenerator<RegexValidator> {

    //https://www.66zan.cn/regexdso/
    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(validator));
        pipeline.append(this.check(validator,validator.getRegex(),validator.getFormatMessage()));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }
}
