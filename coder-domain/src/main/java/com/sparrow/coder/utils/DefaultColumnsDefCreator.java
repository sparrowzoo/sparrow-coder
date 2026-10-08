/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.coder.utils;

import com.alibaba.fastjson.JSON;
import com.sparrow.coder.constant.DefaultSpecialColumnIndex;
import com.sparrow.coder.domain.bo.ColumnDef;
import com.sparrow.coder.domain.bo.validate.*;
import com.sparrow.coder.enums.*;
import com.sparrow.coder.protocol.dto.TableConfigDTO;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.dao.InputDatasource;
import com.sparrow.protocol.dao.enums.DatasourceType;
import com.sparrow.utility.StringUtility;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
            log.error("class not found {}", className, e);
            return Collections.emptyList();
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
            columnDef.setDatasourceType(DatasourceType.NULL.getIdentity());
            columnDef.setDatasourceParams("");
            columnDef.setHeaderType(HeaderType.NORMAL.getIdentity());
            columnDef.setCellType(CellType.NORMAL.getIdentity());
            if (columnDef.getPropertyName().equals("gmtCreate") || columnDef.getPropertyName().equals("gmtModified")) {
                columnDef.setCellType(CellType.UNIX_TIMESTAMP.getIdentity());
            }
            if (entityManager.getPrimary() != null && columnDef.getPropertyName().equals(entityManager.getPrimary().getPropertyName())) {
                columnDef.setControlType(ControlType.INPUT_HIDDEN.getIdentity());
            } else {
                if (columnDef.isNumber()) {
                    columnDef.setDefaultValue("0");
                    columnDef.setValidateType(Validator.SIGNED_DIGITAL);
                } else if (columnDef.isFloat()) {
                    columnDef.setValidateType(Validator.FLOAT);
                } else if (!columnDef.getAllowNull() && columnDef.getJavaType().equals(String.class.getName())) {
                    columnDef.setValidateType(Validator.STRING);
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

            if (field.getInputDatasource() != null) {
                InputDatasource inputDatasource = field.getInputDatasource();
                columnDef.setDatasourceType(inputDatasource.type().getIdentity());
                columnDef.setDatasourceParams(inputDatasource.params());
                if (DatasourceType.TABLE.equals(field.getInputDatasource().type()) ||
                        DatasourceType.ENUM.equals(field.getInputDatasource().type()) ||
                        DatasourceType.DICTIONARY.equals(field.getInputDatasource().type())
                ) {
                    columnDef.setDefaultValue(inputDatasource.defaultValue());
                    columnDef.setControlType(ControlType.SELECT.getIdentity());
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
        if (field.getColumnDefinition().contains("text") || field.getColumnDefinition().contains("varchar(512)")) {
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
        if (jsonValidator == null) {
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
        Map<String, ColumnDef> columnMapOfDB = columnDefs.stream().collect(Collectors.toMap(ColumnDef::getPropertyName, c -> c));
        List<ColumnDef> defaultColumnsOfClass = DefaultColumnsDefCreator.create(className, false);
        Map<String, ColumnDef> defaultColumnMap = new HashMap<>();
        //如果数据库不存在则新增配置
        for (ColumnDef defaultColumn : defaultColumnsOfClass) {
            defaultColumnMap.put(defaultColumn.getPropertyName(), defaultColumn);
            if (!columnMapOfDB.containsKey(defaultColumn.getPropertyName())) {
                columnMapOfDB.put(defaultColumn.getPropertyName(), defaultColumn);
            }
        }
        Iterator<Map.Entry<String, ColumnDef>> columnDefITOfDB = columnMapOfDB.entrySet().iterator();
        while (columnDefITOfDB.hasNext()) {
            Map.Entry<String, ColumnDef> currentColumnEntry = columnDefITOfDB.next();
            //如果列被移除，则直接删除
            if (!defaultColumnMap.containsKey(currentColumnEntry.getKey())) {
                columnDefITOfDB.remove();
                continue;
            }
            ColumnDef currentColumn = currentColumnEntry.getValue();
            ColumnDef defaultColumn = defaultColumnMap.get(currentColumn.getPropertyName());
            //如果存在，则需要更新
            currentColumn.setJavaType(defaultColumn.getJavaType());
            if (defaultColumn.getDatasourceType() != null) {
                currentColumn.setDatasourceType(defaultColumn.getDatasourceType());
                currentColumn.setDatasourceParams(defaultColumn.getDatasourceParams());
                if (DatasourceType.TABLE.getIdentity().equals(defaultColumn.getDatasourceType()) ||
                        DatasourceType.ENUM.getIdentity().equals(defaultColumn.getDatasourceType()) ||
                        DatasourceType.DICTIONARY.getIdentity().equals(defaultColumn.getDatasourceType())
                ) {
                    currentColumn.setControlType(ControlType.SELECT.getIdentity());
                }
            }
        }
        List<ColumnDef> newColumnDefs = new ArrayList<>(columnMapOfDB.values());
        newColumnDefs.sort(Comparator.comparingInt(ColumnDef::getSort));
        return newColumnDefs;
    }
}
