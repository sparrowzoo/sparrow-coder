package com.sparrowzoo.coder.domain.service.frontend.validate.react;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.validate.RegexValidator;

import javax.inject.Named;

@Named
public class IdCardValidatorMessageGenerator extends RegexValidatorMessageGenerator {

    //https://www.66zan.cn/regexdso/
    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("/^(\\d{15}|\\d{17}[\\dXx])$/");
        if(StringUtility.isNullOrEmpty(validator.getFormatMessage())){
            validator.setFormatMessage("请输入正确的身份证号码");
        }
        return super.outerGenerateMessage(propertyName, validator);
    }

    @Override
    public RegexValidator defaultValidator() {
        RegexValidator validator=RegexValidator.REGEX_VALIDATOR.create();
        validator.setFormatMessage("请输入正确的身份证号码");
        return validator;
    }
}
