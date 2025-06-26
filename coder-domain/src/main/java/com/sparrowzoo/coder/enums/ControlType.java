package com.sparrowzoo.coder.enums;

import lombok.Getter;

@Getter
public enum ControlType {
    LABEL("lbl","label"),
    LINK("lnk","link"),
    INPUT_TEXT("txt","text"),
    INPUT_HIDDEN("hdn","hidden"),
    INPUT_PASSWORD("txt","password"),
    TEXT_AREA("txt","textarea"),
    SELECT("ddl","select"),
    RADIO_LIST("rdl","radio-list"),
    CHECK_BOX_LIST("cbl","checkbox-list"),
    CODE("txt","text"),
    EDITOR("divEditor","editor"),
    DATE("txt","date"),
    DATE_HHMMSS("txt","time"),
    CHECK_BOX("ckb","checkbox"),
    RADIO("rdb","radio"),
    FILE("flb","file"),
    ENABLE_DISABLE("","button"),
    SLIDER("sld","slider"),
    IMAGE("img","image");


    private final String prefix;
    private final String inputType;

    ControlType(String prefix,String inputType) {
        this.prefix = prefix;
        this.inputType = inputType;
    }
}
