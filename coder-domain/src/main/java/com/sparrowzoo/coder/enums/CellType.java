package com.sparrowzoo.coder.enums;

import com.sparrow.protocol.EnumIdentityAccessor;
import com.sparrow.protocol.EnumUniqueName;
import com.sparrowzoo.coder.constant.EnumNames;
import lombok.Getter;

@Getter
@EnumUniqueName(name = EnumNames.CELL_TYPE)
public enum CellType implements EnumIdentityAccessor {
    NORMAL("NormalCell", "normal", "标准", 1),
    TREE("TreeCell", "tree", "树形", 2),
    OPERATION("OperationCell", "operation", "命令操作", 3),
    CHECK_BOX("CheckBoxCell", "check-box", "选择", 4),
    CURRENCY("CurrencyCell", "currency", "货币", 5);

    private String componentName;
    private String fileName;
    private String description;
    private Integer id;


    CellType(String componentName, String fileName, String description, Integer id) {
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
