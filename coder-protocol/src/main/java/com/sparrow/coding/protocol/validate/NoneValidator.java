package com.sparrow.coding.protocol.validate;

public class NoneValidator implements Validator {

    @Override
    public Boolean getI18n() {
        return false;
    }

    @Override
    public Boolean getAllowEmpty() {
        return true;
    }

    @Override
    public String getEmptyMessage() {
        return "";
    }
}
