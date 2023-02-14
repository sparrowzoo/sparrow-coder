package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.protocol.validate.NullValidator;
import com.sparrow.utility.StringUtility;

public class NullMessageGenerator implements ValidatorMessageGenerator<NullValidator> {
    @Override
    public String generateValidateMessage(String fieldName, String controlPrefix, NullValidator validator) {
        String upperFieldName = StringUtility.setFirstByteUpperCase(fieldName);
        String validatorKey = ValidatorMessageUtils.getFieldKey(controlPrefix, upperFieldName);
        String errorCtrlId = ValidatorMessageUtils.getErrorCtrlId(upperFieldName);
        String prompt = ValidatorMessageUtils.formatMessage("prompt", validator.prompt());
        String nullError = ValidatorMessageUtils.formatMessage("nullError", validator.nullError());
        String minLength = ValidatorMessageUtils.formatMessage("minLength", validator.minLength());
        String maxLength = ValidatorMessageUtils.formatMessage("maxLength", validator.maxLength());
        String lengthError = ValidatorMessageUtils.formatMessage("lengthError", validator.lengthError());
        String allowNull = ValidatorMessageUtils.formatMessage("allowNull", validator.allowNull());

        StringBuilder sb = new StringBuilder();
        sb.append(validatorKey);
        sb.append(errorCtrlId);
        sb.append(prompt);
        sb.append(nullError);
        sb.append(minLength);
        sb.append(maxLength);
        sb.append(lengthError);
        sb.append(allowNull);
        if (!StringUtility.isNullOrEmpty(validator.defaultValue())) {
            String defaultValue = ValidatorMessageUtils.formatMessage("defaultValue", validator.defaultValue());
            sb.append(defaultValue);
        }
        ValidatorMessageUtils.finish(sb);
        return sb.toString();
    }
}
