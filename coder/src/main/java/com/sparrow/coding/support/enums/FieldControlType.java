package com.sparrow.coding.support.enums;

public enum FieldControlType {

    TextField("txt"),

    Code("txt"),

    TextArea("txt"),

    Editor("divEditor"),

    Date("txt"),

    DateHHMMSS("txt"),

    Password("txt"),

    Checkbox("ckb"),

    Radio("rdb"),

    Hidden("hdn"),

    Select("slt"),

    MultiSelect("cmb"),

    File("flb"),

    RadioBoxList("rbl"),

    CheckBoxList("cbl"),

    Button("btn");

    private String prefix;

    FieldControlType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

}