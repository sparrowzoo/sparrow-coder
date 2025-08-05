package com.sparrowzoo.coder.domain.service.frontend.validate.react;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.validate.RegexValidator;

import javax.inject.Named;

@Named
public class TelValidatorMessageGenerator extends RegexValidatorMessageGenerator {

    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        if(StringUtility.isNullOrEmpty(validator.getFormatMessage())){
            validator.setFormatMessage(this.defaultValidator.getFormatMessage());
        }
        validator.setRegex("/^(\\d{4}-|\\d{3}-)?(\\d{8}|\\d{7})$/");
        return super.outerGenerateMessage(propertyName, validator);
    }

    @Override
    public RegexValidator defaultValidator() {
        RegexValidator validator=super.defaultValidator();
        validator.setFormatMessage("请输入正确的电话号码");
        return validator;
    }
}
