package com.sparrow.coding.protocol;

public enum ControlType {
    INPUT_TEXT("txt"),
    INPUT_PASSWORD("txt"),
    TEXT_AREA("txt"),
    DROPDOWN_LIST("ddl"),
    RADIO_LIST("rdb"),
    CHECK_BOX_LIST("chb"),
    IMAGE("img");

    private String prefix;

    ControlType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
