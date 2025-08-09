package com.sparrowzoo.coder.domain.service.frontend.validate.react;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.validate.RegexValidator;
import com.sparrowzoo.coder.domain.bo.validate.StringValidator;

import javax.inject.Named;

@Named
public class EmailValidatorMessageGenerator extends AbstractValidatorMessageGenerator<RegexValidator> {

    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        StringBuilder pipeline = new StringBuilder();
        pipeline.append(this.pipeline());
        pipeline.append(this.nonEmpty(propertyName,validator));
        pipeline.append(this.minLength(propertyName,validator));
        pipeline.append(this.maxLength(propertyName,validator));
        pipeline.append(this.email(propertyName,validator));
        this.finish(pipeline);
        if (validator.getAllowEmpty()) {
            return this.allowEmpty(pipeline.toString());
        }
        return pipeline.toString();
    }

    private String email(String propertyName, RegexValidator validator) {
        String message = validator.getFormatMessage();
        if (StringUtility.isNullOrEmpty(message)) {
            message = this.defaultValidator.getFormatMessage();
        }
        return String.format(",\nv.email(%s)", this.getMessage(propertyName,validator, "email-message", message));
    }

    @Override
    public RegexValidator defaultValidator() {
        RegexValidator validator=RegexValidator.REGEX_VALIDATOR.create();
        validator.setFormatMessage("请输入有效的邮箱地址");
        return validator;
    }
}
