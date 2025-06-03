package com.sparrow.coding.protocol.validate;

import lombok.Data;

@Data
public class RegexValidator extends StringValidator {
    private Boolean i18n;
    private String formatMessage;
}
