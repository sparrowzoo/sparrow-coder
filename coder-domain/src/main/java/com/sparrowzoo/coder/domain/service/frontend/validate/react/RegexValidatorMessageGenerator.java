package com.sparrowzoo.coder.domain.service.frontend.validate.react;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.validate.RegexValidator;

import javax.inject.Named;

@Named
public class RegexValidatorMessageGenerator extends AbstractValidatorMessageGenerator<RegexValidator> {

    //https://www.66zan.cn/regexdso/
    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(propertyName,validator));
        if (StringUtility.isNullOrEmpty(validator.getFormatMessage())) {
            validator.setFormatMessage(this.defaultValidator.getFormatMessage() + validator.getRegex());
        }
        pipeline.append(this.check(propertyName,validator, validator.getRegex(), validator.getFormatMessage()));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }

    @Override
    public RegexValidator defaultValidator() {
        return RegexValidator.REGEX_VALIDATOR;
    }
}
