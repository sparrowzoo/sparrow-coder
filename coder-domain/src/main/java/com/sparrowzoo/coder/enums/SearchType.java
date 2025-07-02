package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.NameAccessor;

public enum SearchType implements NameAccessor {
    EQUAL,
    PREFIX_LIKE,
    SUFFIX_LIKE,
    LIKE,
    DATE_RANGE,
    LESS,
    LESS_EQUAL,
    GREATER,
    GREATER_EQUAL,
    BETWEEN,
    NOT_EQUAL;

    @Override
    public String getName() {
        return this.name();
    }
}
