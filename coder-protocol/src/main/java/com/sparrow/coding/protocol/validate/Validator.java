package com.sparrow.coding.protocol.validate;

public interface Validator {
    Boolean getI18n();

    Boolean getAllowEmpty();

    String getEmptyMessage();
}
