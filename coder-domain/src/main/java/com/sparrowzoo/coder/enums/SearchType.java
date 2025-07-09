package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;

@EnumUniqueName(name = EnumNames.SEARCH_TYPE)
public enum SearchType implements EnumIdentityAccessor {
    EQUAL(1),
    PREFIX_LIKE(2),
    SUFFIX_LIKE(3),
    LIKE(4),
    DATE_RANGE(5),
    LESS(6),
    LESS_EQUAL(7),
    GREATER(8),
    GREATER_EQUAL(9),
    BETWEEN(10),
    NOT_EQUAL(11);

    private Integer id;

    SearchType(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public static SearchType getById(Integer id) {
        for (SearchType type : SearchType.values()) {
            if (type.getIdentity().equals(id)) {
                return type;
            }
        }
        return null;
    }
}
