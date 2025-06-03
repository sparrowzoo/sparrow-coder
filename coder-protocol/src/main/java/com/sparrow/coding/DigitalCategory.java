package com.sparrow.coding;

public enum DigitalCategory {
    INTEGER("/^\\d+$/"),
    SIGNED_INTEGER("/^-?\\d+$/"),
    FLOAT("/^-?\\d+\\.\\d+$/");
    private final String regex;

    DigitalCategory(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
