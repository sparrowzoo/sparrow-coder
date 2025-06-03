package com.sparrow.coding.protocol.validate;

import lombok.Data;

@Data
public class StringValidator implements Validator {
    private Boolean i18n;
    private String emptyMessage;
    private boolean allowEmpty;
    protected int minLength;
    protected int maxLength;
    private String lengthMessage;
}
