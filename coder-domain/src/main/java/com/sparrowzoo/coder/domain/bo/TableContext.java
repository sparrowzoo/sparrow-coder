package com.sparrowzoo.coder.domain.bo;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.validate.DigitalValidator;
import com.sparrowzoo.coder.domain.bo.validate.NoneValidator;
import com.sparrowzoo.coder.enums.*;
import lombok.Data;

import java.util.*;

@Data
public class TableContext {
    private Map<String, String> placeHolder;
    private TableConfigBO tableConfig;
    private EntityManager entityManager;
    private Map<String, Object> i18nMap = new HashMap<>();

    public TableContext(TableConfigBO tableConfig) {
        this.tableConfig = tableConfig;
        try {
            this.entityManager = new SparrowEntityManager(Class.forName(tableConfig.getClassName()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.initErrorMessage();
    }

    public void initErrorMessage() {
        i18nMap.putIfAbsent("ErrorMessage", new HashMap<>());
        Map<String, String> errorMessages = (Map<String, String>) i18nMap.get("ErrorMessage");
        errorMessages.put(SparrowError.SYSTEM_SERVER_ERROR.name().toLowerCase(), "系统错误，请稍侯再试...");
        errorMessages.put(SparrowError.GLOBAL_PARAMETER_NULL.name().toLowerCase(), "参数不能为空...");
    }

    public Map<String, String> getValidateI18nMap(String propertyName) {
        i18nMap.putIfAbsent("validate", new HashMap<>());
        Map<String, Object> i18nMessages = (Map<String, Object>) i18nMap.get("validate");
        i18nMessages.putIfAbsent(propertyName, new HashMap<>());
        return (Map<String, String>) i18nMessages.get(propertyName);
    }

    public String getPoPackage() {
        String className = tableConfig.getClassName();
        return className.substring(0, className.lastIndexOf("."));
    }

    private String parseChineseName(String comment) {
        int commentIndex = comment.toLowerCase().indexOf("comment");
        if (commentIndex < 0) {
            return "";
        }
        String chineseName = comment.substring(commentIndex + 8);
        if (StringUtility.isNullOrEmpty(chineseName)) {
            return "";
        }
        return chineseName.replace("\"", "").replace("'", "");
    }


    public DigitalValidator generatePrimaryKeyValidator(String propertyName) {
        DigitalValidator validator = new DigitalValidator();
        validator.setAllowEmpty(true);
        validator.setI18n(false);
        validator.setMinValue(null);
        validator.setMaxValue(null);
        validator.setCategory(DigitalCategory.INTEGER);
        validator.setPropertyName(propertyName);
        return validator;
    }

    public List<ColumnDef> getDefaultColumns() {
        String tableClassName = this.getEntityManager().getSimpleClassName();

        List<ColumnDef> columnDefs = new ArrayList<>();
        Map<String, Field> fieldMap = this.getEntityManager().getPropertyFieldMap();
        int i = 0;
        for (String propertyName : fieldMap.keySet()) {
            Field field = fieldMap.get(propertyName);
            ColumnDef columnDef = new ColumnDef();
            columnDef.setTableClassName(tableClassName);
            columnDef.setPropertyName(propertyName);
            columnDef.setChineseName(this.parseChineseName(field.getColumnDefinition()));
            columnDef.setSubsidiaryColumns("");
            columnDef.setJavaType(field.getType().getName());
            columnDef.setEnableHidden(true);
            columnDef.setDefaultHidden(false);
            if (entityManager.getPoPropertyNames() != null && entityManager.getPoPropertyNames().contains(field.getPropertyName())) {
                columnDef.setShowInEdit(false);
                columnDef.setReadOnly(true);
            } else {
                columnDef.setShowInEdit(true);
            }
            columnDef.setShowInList(true);
            columnDef.setShowInSearch(false);
            columnDef.setAllowNull(false);
            columnDef.setPlaceholder("");
            columnDef.setDefaultValue("");
            columnDef.setSearchType(SearchType.EQUAL);
            columnDef.setValidateType(null);
            columnDef.setValidator(new NoneValidator());
            columnDef.setDataSourceType(DataSourceType.NULL);
            columnDef.setDataSourceApi("");
            columnDef.setDataSourceParams("");
            columnDef.setColumnType(ColumnType.NORMAL);
            columnDef.setHeaderType(HeaderType.NORMAL);
            columnDef.setCellType(CellType.NORMAL);
            if (columnDef.getPropertyName().equals(entityManager.getPrimary().getPropertyName())) {
                columnDef.setControlType(ControlType.INPUT_HIDDEN);
                columnDef.setValidateType("digitalValidatorMessageGenerator");
                columnDef.setValidator(this.generatePrimaryKeyValidator(columnDef.getPropertyName()));
            } else {
                columnDef.setControlType(JavaTypeController.getByJavaType(columnDef.getJavaType()).getControlTypes()[0]);
            }
            columnDef.setSort(i++);
            columnDefs.add(columnDef);
        }
        TableConfigBO tableConfig = this.getTableConfig();
        if (tableConfig.getColumnFilter() >= 0) {
            columnDefs.add(ColumnDef.createFilter(tableClassName, tableConfig.getColumnFilter()));
        }
        if (tableConfig.getCheckable() >= 0) {
            columnDefs.add(ColumnDef.createCheckBox(tableClassName, tableConfig.getCheckable()));
        }
        if (tableConfig.getRowMenu() >= 0) {
            columnDefs.add(ColumnDef.createRowMenu(tableClassName, tableConfig.getRowMenu()));
        }
        columnDefs.sort(Comparator.comparingInt(ColumnDef::getSort));
        return columnDefs;
    }
}
