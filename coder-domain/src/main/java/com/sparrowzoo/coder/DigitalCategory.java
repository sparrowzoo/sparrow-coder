package com.sparrow.coding;

public enum DigitalCategory {
    INTEGER("/^\\d+$/","parseInt(input,10)"),
    SIGNED_INTEGER("/^-?\\d+$/","parseInt(input,10)"),
    FLOAT("/^-?\\d+\\.\\d+$/","parseFloat(input)");
    private final String regex;
    private final String converter;

    DigitalCategory(String regex,String converter) {
        this.regex = regex;
        this.converter = converter;
    }

    public String getRegex() {
        return regex;
    }

    public String getConverter() {
        return converter;
    }
}
