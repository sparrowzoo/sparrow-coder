package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.protocol.validate.DigitalValidator;
import com.sparrow.utility.StringUtility;

public class DigitalValidatorMessageGenerator implements ValidatorMessageGenerator<DigitalValidator> {
    @Override
    public String generateValidateMessage(String fieldName, String controlPrefix, DigitalValidator validator) {
        String upperFieldName = StringUtility.setFirstByteUpperCase(fieldName);
        String validateKey = controlPrefix + upperFieldName;
        String errorCtrlId = "error" + fieldName;
        return null;
    }
}
