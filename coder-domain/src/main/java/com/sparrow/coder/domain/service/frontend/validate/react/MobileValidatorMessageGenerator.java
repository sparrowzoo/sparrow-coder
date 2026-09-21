package com.sparrow.coder.domain.service.frontend.validate.react;

import com.sparrow.utility.StringUtility;
import com.sparrow.coder.domain.bo.validate.RegexValidator;
import jakarta.inject.Named;

@Named
public class MobileValidatorMessageGenerator extends RegexValidatorMessageGenerator {
    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("/^1\\d{10}$/");
        if(StringUtility.isNullOrEmpty(validator.getFormatMessage())){
            validator.setFormatMessage("请输入正确的手机号码");
        }
        return super.outerGenerateMessage(propertyName, validator);
    }

    @Override
    public RegexValidator defaultValidator() {
        RegexValidator validator=RegexValidator.REGEX_VALIDATOR.create();
        validator.setFormatMessage("请输入正确的手机号码");
        return validator;
    }
}
