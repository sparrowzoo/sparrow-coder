package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.AllowInputCharLengthValidator;
import javax.inject.Named;

@Named
public class AllowInputCharLengthValidatorMessageGenerator extends AbstractValidatorMessageGenerator<AllowInputCharLengthValidator> {
    @Override public Class<AllowInputCharLengthValidator> getValidateAnnotation() {
        return AllowInputCharLengthValidator.class;
    }
}
