package com.sparrow.coder.domain.service.frontend.validate.react;

import com.sparrow.utility.StringUtility;
import com.sparrow.coder.domain.bo.validate.RegexValidator;
import jakarta.inject.Named;

@Named
public class ChineseCharactersValidatorMessageGenerator extends RegexValidatorMessageGenerator {

    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("/^[\\u4e00-\\u9fa5]+$/");
        if(StringUtility.isNullOrEmpty(validator.getFormatMessage())){
            validator.setFormatMessage(this.defaultValidator.getFormatMessage());
        }
        return super.outerGenerateMessage(propertyName, validator);
    }

    @Override
    public RegexValidator defaultValidator() {
        RegexValidator validator=RegexValidator.REGEX_VALIDATOR.create();
        validator.setFormatMessage("请输入中文字符");
        return validator;
    }
}
