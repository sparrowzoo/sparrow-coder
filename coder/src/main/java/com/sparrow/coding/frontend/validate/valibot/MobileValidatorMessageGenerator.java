package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.RegexValidator;

import javax.inject.Named;

@Named
public class MobileValidatorMessageGenerator extends RegexValidatorMessageGenerator {
    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("^1\\d{10}$");
        return super.outerGenerateMessage(propertyName, validator);
    }
}
