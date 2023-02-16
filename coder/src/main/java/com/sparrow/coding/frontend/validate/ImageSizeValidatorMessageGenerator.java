package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.ImageSizeValidator;
import javax.inject.Named;

@Named
public class ImageSizeValidatorMessageGenerator extends AbstractValidatorMessageGenerator<ImageSizeValidator> {

    @Override public Class<ImageSizeValidator> getValidateAnnotation() {
        return ImageSizeValidator.class;
    }
}
