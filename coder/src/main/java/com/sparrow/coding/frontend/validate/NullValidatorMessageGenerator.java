package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.NullValidator;
import javax.inject.Named;

@Named
public class NullValidatorMessageGenerator extends AbstractValidatorMessageGenerator<NullValidator> {

    @Override public Class<NullValidator> getValidateAnnotation() {
        return NullValidator.class;
    }
}
