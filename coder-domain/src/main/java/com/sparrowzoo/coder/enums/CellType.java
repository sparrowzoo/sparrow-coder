package com.sparrowzoo.coder.enums;

public enum CellType {
    CHECK_BOX("CheckboxHeader", "check-box.tsx", "选择"),
    NORMAL("NormalHeader", "normal.tsx", "标准"),
    CURRENCY("CurrencyHeader", "currency.tsx", "货币"),
    TREE("TreeHeader", "tree.tsx", "树形"),

    OPERATION("OperationHeader", "operation.tsx", "命令操作");
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

    CellType(String componentName, String fileName, String description) {
        this.componentName = componentName;
        this.fileName = fileName;
        this.description = description;
    }
}
