package com.sparrowzoo.coder.utils;

import com.alibaba.fastjson.JSON;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.dao.ListDatasource;
import com.sparrow.protocol.dao.enums.ListDatasourceType;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.constant.DefaultSpecialColumnIndex;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.bo.validate.DigitalValidator;
import com.sparrowzoo.coder.domain.bo.validate.NoneValidator;
import com.sparrowzoo.coder.domain.bo.validate.RegexValidator;
import com.sparrowzoo.coder.domain.bo.validate.StringValidator;
import com.sparrowzoo.coder.domain.service.registry.ValidatorRegistry;
import com.sparrowzoo.coder.enums.*;
import com.sparrowzoo.coder.protocol.dto.TableConfigDTO;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultColumnsDefCreator {

    private static Json json = JsonFactory.getProvider();

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
            columnDef.setDatasourceType(ListDatasourceType.NULL.getIdentity());
            columnDef.setDatasourceParams("");
            if (field.getListDatasource() != null) {
                ListDatasource listDatasource = field.getListDatasource();
                columnDef.setDatasourceType(listDatasource.type().getIdentity());
                columnDef.setDatasourceParams(listDatasource.params());
            }

            columnDef.setColumnType(ColumnType.NORMAL.getIdentity());
            columnDef.setHeaderType(HeaderType.NORMAL.getIdentity());
            columnDef.setCellType(CellType.NORMAL.getIdentity());
            if (columnDef.getPropertyName().equals("gmtCreate") ||
                    columnDef.getPropertyName().equals("gmtModified")) {
                columnDef.setCellType(CellType.UNIX_TIMESTAMP.getIdentity());
            }
            if (entityManager.getPrimary() != null && columnDef.getPropertyName().equals(entityManager.getPrimary().getPropertyName())) {
                columnDef.setControlType(ControlType.INPUT_HIDDEN.getIdentity());

            } else {
                if (columnDef.isNumber()) {
                    columnDef.setValidateType(DigitalValidator.NAME);
                } else if (!columnDef.getAllowNull() && columnDef.getJavaType().equals(String.class.getName())) {
                    columnDef.setValidateType(StringValidator.NAME);
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
        for (int i = 0; i < columnDefs.size(); i++) {
            columnDefs.get(i).setSort(i);
        }
        return columnDefs;
    }

    public static void fillTableLevelColumn(List<ColumnDef> columnDefs, String tableClassName) {
        List<ColumnDef> persistentColumns = create(tableClassName, true);
        int sort = 100;
        for (ColumnDef columnDef : persistentColumns) {
            columnDef.setSort(sort++);
        }
        columnDefs.addAll(persistentColumns);
        columnDefs.add(ColumnDef.createFilter(DefaultSpecialColumnIndex.COLUMN_FILTER));
        columnDefs.add(ColumnDef.createCheckBox(DefaultSpecialColumnIndex.CHECK));
        columnDefs.add(ColumnDef.createRowMenu(DefaultSpecialColumnIndex.ROW_MENU));
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


    public static void resetColumns(List<TableConfigDTO> tableConfigs) {
        for (TableConfigDTO tableConfig : tableConfigs) {
            String className = tableConfig.getClassName();
            List<ColumnDef> columnDefs = parseColumnDefs(tableConfig.getColumnConfigs(), tableConfig.getClassName());
            columnDefs = resetColumns(className, columnDefs);
            tableConfig.setColumnConfigs(JSON.toJSONString(columnDefs));
        }
    }

    public static void parseValidator(ColumnDef columnDef, Object jsonValidator) {
        if(jsonValidator==null){
            return;
        }
        String validateType = columnDef.getValidateType();
        if (StringUtility.isNullOrEmpty(validateType) || validateType.startsWith("nullable")) {
            NoneValidator noneValidator = new NoneValidator(columnDef.getJavaType());
            noneValidator.setClazz(columnDef.getJavaType());
            columnDef.setValidator(noneValidator);
            return;
        }
        if (validateType.startsWith("digital")) {
            DigitalValidator validatorString = json.toJavaObject(jsonValidator, DigitalValidator.class);
            columnDef.setValidator(validatorString);
            return;
        }
        if (validateType.startsWith("string")) {
            StringValidator validatorString = json.toJavaObject(jsonValidator, StringValidator.class);
            columnDef.setValidator(validatorString);
            return;
        }
        RegexValidator validatorString = json.toJavaObject(jsonValidator, RegexValidator.class);
        columnDef.setValidator(validatorString);
    }


    public static List<ColumnDef> parseColumnDefs(String columnConfigs, String className) {

        List<ColumnDef> columnDefs = new ArrayList<>();
        List<Object> jsonObjects = json.parseArray(columnConfigs);
        for (Object jsonObject : jsonObjects) {
            ColumnDef columnDef = json.toJavaObject(jsonObject, ColumnDef.class);
            columnDefs.add(columnDef);
            Object jsonValidator = json.getJSONObject(jsonObject, "validator");
            parseValidator(columnDef, jsonValidator);
        }
        return columnDefs;
    }

    private static List<ColumnDef> resetColumns(String className, List<ColumnDef> columnDefs) {
        Map<String, ColumnDef> allColumnMap = columnDefs.stream().collect(Collectors.toMap(ColumnDef::getPropertyName, c -> c));
        List<ColumnDef> defaultColumnsOfClass = DefaultColumnsDefCreator.create(className, false);
        Map<String, ColumnDef> defaultColumnMap = new HashMap<>();
        //如果数据库不存在则新增配置
        for (ColumnDef defaultColumn : defaultColumnsOfClass) {
            defaultColumnMap.put(defaultColumn.getPropertyName(), defaultColumn);
            if (!allColumnMap.containsKey(defaultColumn.getPropertyName())) {
                allColumnMap.put(defaultColumn.getPropertyName(), defaultColumn);
            }
        }
        Iterator<Map.Entry<String, ColumnDef>> columnDefIterator = allColumnMap.entrySet().iterator();
        while (columnDefIterator.hasNext()) {
            Map.Entry<String, ColumnDef> currentColumnEntry = columnDefIterator.next();
            //如果列被移除，则直接删除
            if (!defaultColumnMap.containsKey(currentColumnEntry.getKey())) {
                columnDefIterator.remove();
                continue;
            }
            ColumnDef currentColumn = currentColumnEntry.getValue();
            ColumnDef defaultColumn = defaultColumnMap.get(currentColumn.getPropertyName());
            //如果存在，则需要更新
            currentColumn.setDatasourceType(defaultColumn.getDatasourceType());
            currentColumn.setDatasourceParams(defaultColumn.getDatasourceParams());
            currentColumn.setJavaType(defaultColumn.getJavaType());
        }
        List<ColumnDef> newColumnDefs = new ArrayList<>(allColumnMap.values());
        newColumnDefs.sort(Comparator.comparingInt(ColumnDef::getSort));
        return newColumnDefs;
    }
}
