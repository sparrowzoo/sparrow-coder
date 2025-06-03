package com.sparrow.coding.protocol.validate;

import lombok.Data;

@Data
public class RegexValidator extends StringValidator {
    private String formatMessage;
}
