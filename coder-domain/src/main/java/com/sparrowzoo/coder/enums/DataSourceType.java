package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;

@EnumUniqueName(name = EnumNames.DATASOURCE_TYPE)
public enum DataSourceType implements EnumIdentityAccessor {
    NULL(1),
    DICTIONARY(2),
    TABLE(3),
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
