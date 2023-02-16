package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.IdCardValidator;
import javax.inject.Named;

@Named
public class IdCardValidatorMessageGenerator extends AbstractValidatorMessageGenerator<IdCardValidator> {

    @Override public Class<IdCardValidator> getValidateAnnotation() {
        return IdCardValidator.class;
    }
}
