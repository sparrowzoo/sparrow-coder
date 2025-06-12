package com.sparrow.coder.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.RegexValidator;

import javax.inject.Named;

@Named
public class IdCardValidatorMessageGenerator extends RegexValidatorMessageGenerator {

    //https://www.66zan.cn/regexdso/
    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("^(\\d{15}|\\d{17}[\\dXx])$");
        return super.outerGenerateMessage(propertyName, validator);
    }
}
