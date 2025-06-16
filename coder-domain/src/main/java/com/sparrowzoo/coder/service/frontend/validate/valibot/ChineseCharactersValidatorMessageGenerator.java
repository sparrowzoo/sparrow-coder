package com.sparrowzoo.coder.service.frontend.validate.valibot;

import com.sparrowzoo.coder.bo.validate.RegexValidator;

import javax.inject.Named;

@Named
public class ChineseCharactersValidatorMessageGenerator extends RegexValidatorMessageGenerator {

    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("/^[\\u4e00-\\u9fa5]+/");
        return super.outerGenerateMessage(propertyName, validator);
    }
}
