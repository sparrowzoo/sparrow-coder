package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.ImageLengthValidator;
import javax.inject.Named;

@Named
public class ImageLengthValidatorMessageGenerator extends AbstractValidatorMessageGenerator<ImageLengthValidator> {
    @Override public Class<ImageLengthValidator> getValidateAnnotation() {
        return ImageLengthValidator.class;
    }
}
