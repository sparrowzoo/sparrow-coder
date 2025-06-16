package com.sparrowzoo.coder.enums;

public enum ControlType {
    LABEL("lbl"),
    LINK("lnk"),
    INPUT_TEXT("txt"),
    INPUT_HIDDEN("hdn"),
    INPUT_PASSWORD("txt"),
    TEXT_AREA("txt"),
    DROPDOWN_LIST("ddl"),
    RADIO_LIST("rdl"),
    CHECK_BOX_LIST("cbl"),
    CODE("txt"),
    EDITOR("divEditor"),
    DATE("txt"),
    DATE_HHMMSS("txt"),
    CHECK_BOX("ckb"),
    RADIO("rdb"),
    SELECT("slt"),
    FILE("flb"),
    ENABLE_DISABLE(""),
    IMAGE("img");


    private final String prefix;

    ControlType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
