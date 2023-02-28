package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.protocol.validate.ChineseCharactersValidator;
import javax.inject.Named;

@Named
public class ChineseCharactersValidatorMessageGenerator extends AbstractValidatorMessageGenerator<ChineseCharactersValidator> {

    @Override public Class<ChineseCharactersValidator> getValidateAnnotation() {
        return ChineseCharactersValidator.class;
    }
}
