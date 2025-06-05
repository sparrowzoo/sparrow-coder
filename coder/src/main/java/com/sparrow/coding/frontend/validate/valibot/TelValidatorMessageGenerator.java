package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.RegexValidator;

import javax.inject.Named;

@Named
public class TelValidatorMessageGenerator extends RegexValidatorMessageGenerator {

    @Override
    public String outerGenerateMessage(String propertyName, RegexValidator validator) {
        validator.setRegex("^(\\d{4}-|\\d{3}-)?(\\d{8}|\\d{7})$");
        return super.outerGenerateMessage(propertyName, validator);
    }
}
