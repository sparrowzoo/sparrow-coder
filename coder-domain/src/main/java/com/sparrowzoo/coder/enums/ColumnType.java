package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;
import lombok.Getter;

@Getter
@EnumUniqueName(name = EnumNames.COLUMN_TYPE)
public enum ColumnType implements EnumIdentityAccessor {
    ACTION(1, "菜单"),
    CHECK(2, "选择"),
    FILTER(3, "过滤列"),
    TREE(4, "树型"),
    NORMAL(5, "标准");

    private String description;

    private Integer id;

    ColumnType(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    @Override
    public Integer getIdentity() {
        return this.id;
    }

    public static ColumnType getById(Integer id) {
        for (ColumnType type : ColumnType.values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }
}
