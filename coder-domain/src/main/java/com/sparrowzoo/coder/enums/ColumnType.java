package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.NameAccessor;
import lombok.Getter;

@Getter
public enum ColumnType implements NameAccessor {
    ACTION("菜单"),
    CHECK("选择"),
    FILTER("过滤列"),
    TREE("树型"),
    NORMAL("正常");

    private String description;

    ColumnType(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
