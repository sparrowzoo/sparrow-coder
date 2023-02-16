package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.AllowOptionsValidator;
import javax.inject.Named;

@Named
public class AllowOptionValidatorMessageGenerator extends AbstractValidatorMessageGenerator<AllowOptionsValidator> {
    @Override public Class<AllowOptionsValidator> getValidateAnnotation() {
        return AllowOptionsValidator.class;
    }
}
