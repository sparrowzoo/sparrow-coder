package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.EqualValidator;
import javax.inject.Named;

@Named
public class EqualValidatorMessageGenerator extends AbstractValidatorMessageGenerator<EqualValidator> {
    @Override public Class<EqualValidator> getValidateAnnotation() {
        return EqualValidator.class;
    }
}
