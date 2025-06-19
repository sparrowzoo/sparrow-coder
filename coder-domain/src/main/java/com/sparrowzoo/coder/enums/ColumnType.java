package com.sparrowzoo.coder.enums;

import lombok.Getter;

@Getter
public enum ColumnType {
    ACTION("菜单"),
    CHECK("选择"),
    FILTER("过滤列"),
    TREE("树型"),
    NORMAL("正常");

    private String description;

    ColumnType(String description) {
        this.description = description;
    }
}
