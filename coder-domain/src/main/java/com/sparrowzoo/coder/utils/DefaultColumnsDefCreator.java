package com.sparrowzoo.coder.utils;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.dao.ListDatasource;
import com.sparrow.protocol.dao.enums.ListDatasourceType;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.constant.DefaultSpecialColumnIndex;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.validate.DigitalValidator;
import com.sparrowzoo.coder.domain.bo.validate.NoneValidator;
import com.sparrowzoo.coder.domain.bo.validate.StringValidator;
import com.sparrowzoo.coder.enums.*;

import java.util.*;

public class DefaultColumnsDefCreator {
    public static List<ColumnDef> create(String className) {
        return create(className, false);
    }

    public static List<ColumnDef> create(String className, Boolean basicPersistence) {
        EntityManager entityManager = null;
        try {
            entityManager = new SparrowEntityManager(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<ColumnDef> columnDefs = new ArrayList<>();
        Map<String, Field> fieldMap = entityManager.getPropertyFieldMap();
        Set<String> poPropertyNames = entityManager.getPoPropertyNames();
        for (String propertyName : fieldMap.keySet()) {
            if (basicPersistence && !poPropertyNames.contains(propertyName)) {
                continue;
            }
            if (!basicPersistence && poPropertyNames.contains(propertyName)) {
                continue;
            }
            Field field = fieldMap.get(propertyName);
            ColumnDef columnDef = new ColumnDef();
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
            columnDef.setShowInList(true);
            columnDef.setShowInSearch(false);
            columnDef.setAllowNull(field.getColumn().nullable());
            columnDef.setPlaceholder("");
            columnDef.setDefaultValue("");
            columnDef.setSearchType(SearchType.EQUAL.getIdentity());
            columnDef.setValidateType(null);
            columnDef.setValidator(new NoneValidator(columnDef.getJavaType()));
            columnDef.setDatasourceType(ListDatasourceType.NULL.getIdentity());
            columnDef.setDatasourceParams("");
            if (field.getListDatasource() != null) {
                ListDatasource listDatasource= field.getListDatasource();
                columnDef.setDatasourceType(listDatasource.type().getIdentity());
                columnDef.setDatasourceParams(listDatasource.params());
            }

            columnDef.setColumnType(ColumnType.NORMAL.getIdentity());
            columnDef.setHeaderType(HeaderType.NORMAL.getIdentity());
            columnDef.setCellType(CellType.NORMAL.getIdentity());
            if(columnDef.getPropertyName().equals("gmtCreate")||
                    columnDef.getPropertyName().equals("gmtModified")){
                columnDef.setCellType(CellType.UNIX_TIMESTAMP.getIdentity());
            }
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
            columnDefs.add(columnDef);
        }
        return columnDefs;
    }

    public static void fillTableLevelColumn(List<ColumnDef> columnDefs, String tableClassName) {
        columnDefs.addAll(create(tableClassName, true));
        for (int i = 0; i < columnDefs.size(); i++) {
            columnDefs.get(i).setSort(i);
        }
        columnDefs.add(ColumnDef.createFilter(tableClassName, DefaultSpecialColumnIndex.COLUMN_FILTER));
        columnDefs.add(ColumnDef.createCheckBox(tableClassName, DefaultSpecialColumnIndex.CHECK));
        columnDefs.add(ColumnDef.createRowMenu(tableClassName, DefaultSpecialColumnIndex.ROW_MENU));

        columnDefs.sort(Comparator.comparingInt(ColumnDef::getSort));
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
