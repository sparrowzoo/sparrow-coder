package com.sparrowzoo.coder.domain.bo;

import com.sparrowzoo.coder.domain.bo.validate.Validator;
import com.sparrowzoo.coder.enums.*;
import lombok.Data;

@Data
public class ColumnDef {
    private String tableClassName;
    private String propertyName;
    private String chineseName;
    private String subsidiaryColumns;
    private String javaType;
    private Boolean enableHidden;
    private Boolean defaultHidden;
    private Boolean showInEdit=true;
    private Boolean showInList=true;
    private Boolean showInSearch=true;
    private Boolean allowNull;
    private String placeholder;
    private String defaultValue;
    /**
     * 查询方式
     */
    private SearchType searchType;
    private String validateType;
    private Validator validator;
    private DataSourceType dataSourceType;
    /**
     * 后台接口
     */
    private String dataSourceApi;
    private String dataSourceParams;
    private ColumnType columnType;
    private HeaderType headerType;
    private CellType cellType;
    private ControlType controlType;
}
