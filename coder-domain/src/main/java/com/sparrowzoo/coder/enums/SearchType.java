package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;
import lombok.Getter;

@EnumUniqueName(name = EnumNames.SEARCH_TYPE)
@Getter
public enum SearchType implements EnumIdentityAccessor {
    EQUAL(1,"equal"),
    PREFIX_LIKE(2,"startWith"),
    SUFFIX_LIKE(3,"endWith"),
    LIKE(4,"contains"),
    DATE_RANGE(5,""),
    LESS(6,"lessThan"),
    LESS_EQUAL(7,"lessThanEqual"),
    GREATER(8,"greaterThan"),
    GREATER_EQUAL(9,"greaterThanEqual"),
    BETWEEN(10,"between"),
    NOT_EQUAL(11,"notEqual");

    private Integer id;
    private String condition;

    SearchType(Integer id,String condition) {
        this.id = id;
        this.condition = condition;
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
