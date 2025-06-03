package com.sparrow.coding.protocol.validate;

public class NoneValidator implements Validator {

    @Override
    public boolean getI18n() {
        return false;
    }

    @Override
    public boolean isAllowEmpty() {
        return true;
    }
}
