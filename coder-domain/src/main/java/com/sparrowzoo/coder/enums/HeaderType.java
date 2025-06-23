package com.sparrowzoo.coder.enums;

import lombok.Getter;

@Getter
public enum HeaderType {
    CHECK_BOX("CheckboxHeader", "check-box", false, false, "选择"),
    COLUMN_FILTER("ColumnFilter", "column-filter", false, false, "过滤"),
    NORMAL("PlainTextHeader", "plain-text", false, false, "标准"),
    NORMAL_SORT("NormalHeader", "normal", true, false, "标准(可排序)"),
    NORMAL_FILTER("NormalHeader", "normal", false, false, "标准(可过滤)"),
    NORMAL_SORT_FILTER("NormalHeader", "normal", true, true, "标准(可过滤,可排序)"),
    PLAIN_TEXT("PlainTextHeader", "plain-text", false, false, "操作"),
    EMPTY("EmptyHeader", "empty", false, false, "空白");
    private String componentName;
    private String fileName;

    private Boolean sortable;

    private Boolean filterable;

    private String description;

    HeaderType(String componentName, String fileName, Boolean sortable, Boolean filterable, String description) {
        this.componentName = componentName;
        this.fileName = fileName;
        this.description = description;
        this.sortable = sortable;
        this.filterable = filterable;
    }
}
