package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.EmailValidator;
import javax.inject.Named;

@Named
public class EmailValidatorMessageGenerator extends AbstractValidatorMessageGenerator<EmailValidator> {
    @Override public Class<EmailValidator> getValidateAnnotation() {
        return EmailValidator.class;
    }
}
