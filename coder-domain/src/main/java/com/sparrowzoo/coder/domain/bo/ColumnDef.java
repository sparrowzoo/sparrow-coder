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
    private Integer sort;
    private Boolean readOnly;

    public static ColumnDef createRowMenu(String tableClassName,int sort) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setColumnType(ColumnType.ACTION);
        columnDef.setHeaderType(HeaderType.NORMAL);
        columnDef.setCellType(CellType.OPERATION);
        columnDef.setTableClassName(tableClassName);
        columnDef.setPropertyName("actions");
        columnDef.setChineseName("操作");
        columnDef.setSubsidiaryColumns("");
        columnDef.setJavaType(null);
        columnDef.setEnableHidden(false);
        columnDef.setDefaultHidden(false);
        columnDef.setShowInEdit(false);
        columnDef.setShowInList(true);
        columnDef.setShowInSearch(false);
        columnDef.setAllowNull(false);
        columnDef.setPlaceholder("");
        columnDef.setDefaultValue("");
        columnDef.setSearchType(SearchType.EQUAL);
        columnDef.setValidateType("");
        columnDef.setValidator(null);
        columnDef.setDataSourceType(DataSourceType.NULL);
        columnDef.setDataSourceApi("");
        columnDef.setDataSourceParams("");
        columnDef.setControlType(null);
        columnDef.setSort(sort);
        columnDef.setReadOnly(true);
        return columnDef;
    }

    public static ColumnDef createFilter(String tableClassName,int sort) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setColumnType(ColumnType.FILTER);
        columnDef.setHeaderType(HeaderType.COLUMN_FILTER);
        columnDef.setCellType(null);
        columnDef.setTableClassName(tableClassName);
        columnDef.setPropertyName("filter");
        columnDef.setChineseName("过滤列");
        columnDef.setSubsidiaryColumns("");
        columnDef.setJavaType(null);
        columnDef.setEnableHidden(false);
        columnDef.setDefaultHidden(false);
        columnDef.setShowInEdit(false);
        columnDef.setShowInList(true);
        columnDef.setShowInSearch(false);
        columnDef.setAllowNull(false);
        columnDef.setPlaceholder("");
        columnDef.setDefaultValue("");
        columnDef.setSearchType(SearchType.EQUAL);
        columnDef.setValidateType("");
        columnDef.setValidator(null);
        columnDef.setDataSourceType(DataSourceType.NULL);
        columnDef.setDataSourceApi("");
        columnDef.setDataSourceParams("");
        columnDef.setControlType(null);
        columnDef.setSort(sort);
        columnDef.setReadOnly(true);
        return columnDef;
    }

    public static ColumnDef createCheckBox(String tableClassName,int sort) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setColumnType(ColumnType.CHECK);
        columnDef.setHeaderType(HeaderType.CHECK_BOX);
        columnDef.setCellType(CellType.CHECK_BOX);
        columnDef.setTableClassName(tableClassName);
        columnDef.setPropertyName("check-box");
        columnDef.setChineseName("");
        columnDef.setSubsidiaryColumns("");
        columnDef.setJavaType(null);
        columnDef.setEnableHidden(false);
        columnDef.setDefaultHidden(false);
        columnDef.setShowInEdit(false);
        columnDef.setShowInList(true);
        columnDef.setShowInSearch(false);
        columnDef.setAllowNull(false);
        columnDef.setPlaceholder("");
        columnDef.setDefaultValue("");
        columnDef.setSearchType(SearchType.EQUAL);
        columnDef.setValidateType("");
        columnDef.setValidator(null);
        columnDef.setDataSourceType(DataSourceType.NULL);
        columnDef.setDataSourceApi("");
        columnDef.setDataSourceParams("");
        columnDef.setControlType(null);
        columnDef.setSort(sort);
        columnDef.setReadOnly(true);
        return columnDef;
    }
}
