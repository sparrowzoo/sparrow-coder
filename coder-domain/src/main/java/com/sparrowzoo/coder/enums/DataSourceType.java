package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;

public enum DataSourceType implements EnumIdentityAccessor {
    NULL(1),
    DICTIONARY(2),
    API(3),
    ENUM(4);

    private final int identity;

    DataSourceType(Integer identity) {
        this.identity = identity;
    }

    public static DataSourceType getById(int identity) {
        for (DataSourceType type : DataSourceType.values()) {
            if (type.identity == identity) {
                return type;
            }
        }
        return NULL;
    }

    @Override
    public Integer getIdentity() {
        return this.identity;
    }
}
