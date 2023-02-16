package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.TelValidator;
import javax.inject.Named;

@Named
public class TelValidatorMessageGenerator extends AbstractValidatorMessageGenerator<TelValidator> {

    @Override public Class<TelValidator> getValidateAnnotation() {
        return TelValidator.class;
    }
}
