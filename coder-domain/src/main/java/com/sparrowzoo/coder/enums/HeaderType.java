package com.sparrowzoo.coder.enums;

public enum HeaderType {
    CHECK_BOX("CheckboxHeader", "check-box.tsx", "选择"),
    NORMAL("NormalHeader", "normal.tsx", "标准"),
    NORMAL_SORT("NormalHeader", "normal.tsx", "标准(可排序)"),
    NORMAL_FILTER("NormalHeader", "normal.tsx", "标准(可过滤)"),
    NORMAL_SORT_FILTER("NormalHeader", "normal.tsx", "标准(可过滤,可排序)"),
    EMPTY("EmptyHeader", "empty.tsx", "空白");
    private String componentName;
    private String fileName;
    private String description;

    public String getComponentName() {
        return componentName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDescription() {
        return description;
    }

    HeaderType(String componentName, String fileName, String description) {
        this.componentName = componentName;
        this.fileName = fileName;
        this.description = description;
    }


}
