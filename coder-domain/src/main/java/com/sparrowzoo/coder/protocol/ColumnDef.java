package com.sparrow.coding.protocol;

import com.sparrow.coding.protocol.validate.Validator;
import lombok.Data;

@Data
public class ColumnDef {
    private String name;
    private boolean primaryKey;
    private boolean i18n;
    private String text;
    private String sqlType;
    private String javaType;
    private boolean showInInsert;
    private boolean showInEdit;
    private boolean showInList;
    private boolean allowNull;
    private String placeholder;
    private String defaultValue;
    private Condition condition;
    private String validateType;
    private Validator validator;
    private Boolean visible;
    private HeaderType headerType;
    private CellType cellType;
}
