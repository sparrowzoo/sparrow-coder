package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import lombok.Getter;

@Getter
public enum CellType implements EnumIdentityAccessor {
    CHECK_BOX("CheckBoxCell", "check-box", "选择",1),
    NORMAL("NormalCell", "normal", "标准",2),
    CURRENCY("CurrencyCell", "currency", "货币",3),
    TREE("TreeCell", "tree", "树形",4),
    OPERATION("OperationCell", "operation", "命令操作",5);
    private String componentName;
    private String fileName;
    private String description;
    private Integer id;


    CellType(String componentName, String fileName, String description,Integer id) {
        this.componentName = componentName;
        this.fileName = fileName;
        this.description = description;
        this.id = id;
    }

    @Override
    public Integer getIdentity() {
        return id;
    }

    public static CellType getById(Integer id) {
        for (CellType cellType : CellType.values()) {
            if (cellType.getId().equals(id)) {
                return cellType;
            }
        }
        return null;
    }
}
