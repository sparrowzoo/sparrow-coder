package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.protocol.validate.ChineseCharactersValidator;
import javax.inject.Named;

@Named
public class ChineseCharactersValidatorMessageGenerator extends AbstractValidatorMessageGenerator<ChineseCharactersValidator> {

    @Override
    public String generateValidateMessage(String propertyName, ChineseCharactersValidator validator) {
        return null;
    }
}
