package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;

@EnumUniqueName(name = EnumNames.DATASOURCE_TYPE)
public enum DatasourceType implements EnumIdentityAccessor {
    NULL(1),
    DICTIONARY(2),
    /**
     * @JoinTable(name = "t_project_config")
     * 语义相同
     */
    TABLE(3),
    ENUM(4);

    private final int identity;

    DatasourceType(Integer identity) {
        this.identity = identity;
    }

    public static DatasourceType getById(int identity) {
        for (DatasourceType type : DatasourceType.values()) {
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
