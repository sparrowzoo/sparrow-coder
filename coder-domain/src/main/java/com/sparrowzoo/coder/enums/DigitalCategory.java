package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import lombok.Getter;

@Getter
public enum DigitalCategory implements EnumIdentityAccessor {
    INTEGER("/^\\d+$/", "parseInt(input,10)", 1),
    SIGNED_INTEGER("/^-?\\d+$/", "parseInt(input,10)", 2),
    FLOAT("/^-?\\d+\\.\\d+$/", "parseFloat(input)", 3);
    private final String regex;
    private final String converter;
    private final Integer id;

    DigitalCategory(String regex, String converter, Integer id) {
        this.regex = regex;
        this.converter = converter;
        this.id = id;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }
}
