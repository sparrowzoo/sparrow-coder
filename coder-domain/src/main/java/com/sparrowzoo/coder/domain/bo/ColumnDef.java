package com.sparrowzoo.coder.domain.bo;

import com.sparrowzoo.coder.domain.bo.validate.Validator;
import com.sparrowzoo.coder.enums.CellType;
import com.sparrowzoo.coder.enums.ColumnType;
import com.sparrowzoo.coder.enums.ControlType;
import com.sparrowzoo.coder.enums.HeaderType;
import lombok.Data;

@Data
public class ColumnDef {
    private String name;
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
    //是否默认隐藏
    private Boolean hidden;
    private ColumnType columnType;
    private HeaderType headerType;
    private CellType cellType;
    private ControlType controlType;
}
