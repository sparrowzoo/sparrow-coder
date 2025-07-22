package com.sparrowzoo.coder.utils;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.constant.DefaultSpecialColumnIndex;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.validate.DigitalValidator;
import com.sparrowzoo.coder.domain.bo.validate.NoneValidator;
import com.sparrowzoo.coder.domain.bo.validate.StringValidator;
import com.sparrowzoo.coder.enums.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DefaultColumnsDefCreator {
    public static List<ColumnDef> create(String className) {
        EntityManager entityManager = null;
        try {
            entityManager = new SparrowEntityManager(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String tableClassName = entityManager.getSimpleClassName();
        List<ColumnDef> columnDefs = new ArrayList<>();
        Map<String, Field> fieldMap = entityManager.getPropertyFieldMap();
        int i = 0;
        for (String propertyName : fieldMap.keySet()) {
            Field field = fieldMap.get(propertyName);
            ColumnDef columnDef = new ColumnDef();
            columnDef.setTableClassName(tableClassName);
            columnDef.setPropertyName(propertyName);
            columnDef.setChineseName(parseChineseName(field.getColumnDefinition()));
            columnDef.setSubsidiaryColumns("");
            columnDef.setJavaType(field.getType().getName());
            columnDef.setEnableHidden(true);
            columnDef.setDefaultHidden(false);
            if (entityManager.getPoPropertyNames() != null && entityManager.getPoPropertyNames().contains(field.getPropertyName())) {
                columnDef.setShowInEdit(false);
            } else {
                columnDef.setShowInEdit(true);
            }
            if (!field.isUpdatable()) {
                columnDef.setReadOnly(true);
            }
            columnDef.setShowInList(true);
            columnDef.setShowInSearch(false);
            columnDef.setAllowNull(field.getColumn().nullable());
            columnDef.setPlaceholder("");
            columnDef.setDefaultValue("");
            columnDef.setSearchType(SearchType.EQUAL.getIdentity());
            columnDef.setValidateType(null);
            columnDef.setValidator(new NoneValidator(columnDef.getJavaType()));
            columnDef.setDatasourceType(DatasourceType.NULL.getIdentity());
            columnDef.setDatasourceParams("");
            if (field.getJoinTable() != null) {
                columnDef.setDatasourceType(DatasourceType.TABLE.getIdentity());
                columnDef.setDatasourceParams(field.getJoinTable().name());
            }

            columnDef.setColumnType(ColumnType.NORMAL.getIdentity());
            columnDef.setHeaderType(HeaderType.NORMAL.getIdentity());
            columnDef.setCellType(CellType.NORMAL.getIdentity());
            if (entityManager.getPrimary() != null && columnDef.getPropertyName().equals(entityManager.getPrimary().getPropertyName())) {
                columnDef.setControlType(ControlType.INPUT_HIDDEN.getIdentity());
                columnDef.setValidateType("digital");
                columnDef.setValidator(generateDefaultNumberValidator(columnDef.getPropertyName(), true));
            } else {
                if (columnDef.isNumber()) {
                    columnDef.setValidateType("digital");
                    columnDef.setValidator(generateDefaultNumberValidator(columnDef.getPropertyName(), false));
                } else if (!columnDef.getAllowNull() && columnDef.getJavaType().equals(String.class.getName())) {
                    columnDef.setValidateType("string");
                    columnDef.setValidator(generateDefaultStringValidator(columnDef.getPropertyName()));
                }
                if (isTextArea(field)) {
                    columnDef.setShowInList(false);
                    columnDef.setEnableHidden(false);
                    columnDef.setDefaultHidden(true);
                    columnDef.setControlType(ControlType.TEXT_AREA.getIdentity());
                } else {
                    columnDef.setControlType(JavaTypeController.getByJavaType(columnDef.getJavaType()).getControlTypes()[0].getIdentity());
                }
            }
            columnDef.setSort(i++);
            columnDefs.add(columnDef);
        }
        columnDefs.add(ColumnDef.createFilter(tableClassName, DefaultSpecialColumnIndex.COLUMN_FILTER));
        columnDefs.add(ColumnDef.createCheckBox(tableClassName, DefaultSpecialColumnIndex.CHECK));
        columnDefs.add(ColumnDef.createRowMenu(tableClassName, DefaultSpecialColumnIndex.ROW_MENU));
        columnDefs.sort(Comparator.comparingInt(ColumnDef::getSort));
        return columnDefs;
    }

    private static boolean isTextArea(Field field) {
        if (field.getColumnDefinition().contains("text") ||
                field.getColumnDefinition().contains("varchar(512)")) {
            return true;
        }
        return false;
    }

    private static String parseChineseName(String comment) {
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


    public static DigitalValidator generateDefaultNumberValidator(String propertyName, Boolean allowEmpty) {
        DigitalValidator validator = new DigitalValidator();
        validator.setAllowEmpty(allowEmpty);
        validator.setI18n(false);
        validator.setMinValue(null);
        validator.setMaxValue(null);
        validator.setCategory(DigitalCategory.INTEGER);
        validator.setPropertyName(propertyName);
        return validator;
    }

    public static StringValidator generateDefaultStringValidator(String propertyName) {
        StringValidator validator = new StringValidator();
        validator.setAllowEmpty(false);
        validator.setI18n(false);
        validator.setMinLength(null);
        validator.setMaxLength(null);
        validator.setPropertyName(propertyName);
        return validator;
    }
}
