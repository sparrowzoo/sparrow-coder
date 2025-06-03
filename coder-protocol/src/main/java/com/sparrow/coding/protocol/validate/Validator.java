package com.sparrow.coding.protocol.validate;

public interface Validator {
    boolean getI18n();

    boolean isAllowEmpty();

    String getEmptyMessage();
}
