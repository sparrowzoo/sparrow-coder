package com.sparrowzoo.coder.domain.bo;

import com.sparrow.protocol.POJO;
import com.sparrow.protocol.dao.PO;
import com.sparrowzoo.coder.domain.bo.validate.Validator;
import com.sparrowzoo.coder.enums.*;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "column_def")
public class ColumnDef implements POJO {
    @Column(name = "table_class_name", updatable = false, columnDefinition = "varchar(255) comment '类名'")
    private String tableClassName;
    @Column(name = "property_name", updatable = false, columnDefinition = "varchar(255) comment '属性名'")
    private String propertyName;
    @Column(name = "chinese_name", columnDefinition = "varchar(255) comment '中文名'")
    private String chineseName;
    @Column(name = "subsidiary_columns", columnDefinition = "varchar(255) comment '附加列'")
    private String subsidiaryColumns;
    @Column(name = "java_type", updatable = false, columnDefinition = "varchar(255) comment 'java类型'")
    private String javaType;
    @Column(name = "enable_hidden", columnDefinition = "bit(1) comment '允许隐藏'")
    private Boolean enableHidden;
    @Column(name = "default_hidden", columnDefinition = "bit(1) comment '默认隐藏'")
    private Boolean defaultHidden;
    @Column(name = "show_in_edit", columnDefinition = "bit(1) comment '编辑'")
    private Boolean showInEdit = true;
    @Column(name = "show_in_list", columnDefinition = "bit(1) comment '列表'")
    private Boolean showInList = true;
    @Column(name = "show_in_search", columnDefinition = "bit(1) comment '搜索'")
    private Boolean showInSearch = true;
    @Column(name = "allow_null", columnDefinition = "bit(1) comment '允许为空'")
    private Boolean allowNull;
    @Column(name = "placeholder", columnDefinition = "varchar(255) comment '提示'")
    private String placeholder;
    @Column(name = "default_value", columnDefinition = "varchar(255) comment '默认值'")
    private String defaultValue;
    /**
     * 查询方式
     */
    @Column(name = "search_type", columnDefinition = "int comment '查询方式'")
    private Integer searchType;
    @Column(name = "validate_type", columnDefinition = "varchar(255) comment '验证类型'")
    private String validateType;
    @Column(name = "validator", columnDefinition = "varchar(255) comment '验证器'")
    private Validator validator;
    @Column(name = "data_source_type", columnDefinition = "int comment '数据源类型'")
    private Integer dataSourceType;
    @Column(name = "data_source_params", columnDefinition = "varchar(255) comment '数据源参数'")
    private String dataSourceParams;
    @Column(name = "column_type", updatable = false, columnDefinition = "int comment '列类型'")
    private Integer columnType;
    @Column(name = "header_type", columnDefinition = "int comment '表头类型'")
    private Integer headerType;
    @Column(name = "cell_type", columnDefinition = "int comment '单元格类型'")
    private Integer cellType;
    @Column(name = "control_type", columnDefinition = "int comment '控件类型'")
    private Integer controlType;
    @Column(name = "sort", columnDefinition = "int comment '排序'")
    private Integer sort;
    @Column(name = "read_only", columnDefinition = "bit(1) comment '是否只读'")
    private Boolean readOnly;

    public static ColumnDef createRowMenu(String tableClassName, int sort) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setColumnType(ColumnType.ACTION.getIdentity());
        columnDef.setHeaderType(HeaderType.NORMAL.getIdentity());
        columnDef.setCellType(CellType.OPERATION.getIdentity());
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
        columnDef.setSearchType(SearchType.EQUAL.getIdentity());
        columnDef.setValidateType("");
        columnDef.setValidator(null);
        columnDef.setDataSourceType(DataSourceType.NULL.getIdentity());
        columnDef.setDataSourceParams("");
        columnDef.setControlType(null);
        columnDef.setSort(sort);
        columnDef.setReadOnly(true);
        return columnDef;
    }

    public static ColumnDef createFilter(String tableClassName, int sort) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setColumnType(ColumnType.FILTER.getIdentity());
        columnDef.setHeaderType(HeaderType.COLUMN_FILTER.getIdentity());
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
        columnDef.setSearchType(SearchType.EQUAL.getIdentity());
        columnDef.setValidateType("");
        columnDef.setValidator(null);
        columnDef.setDataSourceType(DataSourceType.NULL.getIdentity());
        columnDef.setDataSourceParams("");
        columnDef.setControlType(null);
        columnDef.setSort(sort);
        columnDef.setReadOnly(true);
        return columnDef;
    }

    public static ColumnDef createCheckBox(String tableClassName, int sort) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setColumnType(ColumnType.CHECK.getIdentity());
        columnDef.setHeaderType(HeaderType.CHECK_BOX.getIdentity());
        columnDef.setCellType(CellType.CHECK_BOX.getIdentity());
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
        columnDef.setSearchType(SearchType.EQUAL.getIdentity());
        columnDef.setValidateType("");
        columnDef.setValidator(null);
        columnDef.setDataSourceType(DataSourceType.NULL.getIdentity());
        columnDef.setDataSourceParams("");
        columnDef.setControlType(null);
        columnDef.setSort(sort);
        columnDef.setReadOnly(true);
        return columnDef;
    }

    public Boolean isNumber() {
        return this.javaType.equals("java.lang.Integer") ||
                this.javaType.equals("java.lang.Long") ||
                this.javaType.equals("java.lang.Double") ||
                this.javaType.equals("java.lang.Float");
    }
}
