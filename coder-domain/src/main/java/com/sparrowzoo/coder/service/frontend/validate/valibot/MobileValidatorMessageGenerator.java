package com.sparrowzoo.coder.service.frontend.validate.valibot;

import com.sparrowzoo.coder.bo.validate.RegexValidator;

import javax.inject.Named;

@Named
public class MobileValidatorMessageGenerator extends RegexValidatorMessageGenerator {
    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("^1\\d{10}$");
        return super.outerGenerateMessage(propertyName, validator);
    }
}
