package com.sparrowzoo.coder.enums;

import lombok.Getter;

@Getter
public enum CellType {
    CHECK_BOX("CheckBoxCell", "check-box", "选择"),
    NORMAL("NormalCell", "normal", "标准"),
    CURRENCY("CurrencyCell", "currency", "货币"),
    TREE("TreeCell", "tree", "树形"),
    OPERATION("OperationCell", "operation", "命令操作");
    private String componentName;
    private String fileName;
    private String description;


    CellType(String componentName, String fileName, String description) {
        this.componentName = componentName;
        this.fileName = fileName;
        this.description = description;
    }
}
