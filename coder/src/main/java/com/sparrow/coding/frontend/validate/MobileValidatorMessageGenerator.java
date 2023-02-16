package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.MobileValidator;
import javax.inject.Named;

@Named
public class MobileValidatorMessageGenerator extends AbstractValidatorMessageGenerator<MobileValidator> {
    @Override public Class<MobileValidator> getValidateAnnotation() {
        return MobileValidator.class;
    }
}
