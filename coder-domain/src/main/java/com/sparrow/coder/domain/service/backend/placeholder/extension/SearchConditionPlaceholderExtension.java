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

package com.sparrow.coder.domain.service.backend.placeholder.extension;

import com.sparrow.coder.domain.bo.ColumnDef;
import com.sparrow.coder.domain.bo.TableConfigBO;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.PlaceholderKey;
import com.sparrow.coder.enums.SearchType;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.protocol.dao.enums.DatasourceType;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Named
@Slf4j
public class SearchConditionPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();

        EntityManager entityManager = tableContext.getEntityManager();
        String persistenceClassName = entityManager.getSimpleClassName();
        Map<String, Field> fields = entityManager.getPropertyFieldMap();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);
        List<ColumnDef> columnDefs = tableContext.getColumns();
        Set<String> queryFields = new LinkedHashSet<>();
        List<String> criteriaList = new ArrayList<>();
        TableConfigBO tableConfig = tableContext.getTableConfig();
        List<String> conditionList = new ArrayList<>();

        for (ColumnDef columnDef : columnDefs) {
            Field field = fields.get(columnDef.getPropertyName());
            if (field == null) {
                log.error("field not found: " + columnDef.getPropertyName());
                continue;
            }
            String upperPropertyName = StringUtility.setFirstByteUpperCase(field.getPropertyName());

            if (field.getType().equals(StatusRecord.class)) {
                queryFields.add(String.format("private Integer %1$s;", field.getPropertyName()));
                conditionList.add(String.format("if(%1$sQuery.getStatus()!=null&&%1$sQuery.getStatus()>=0) {booleanCriteria.and(Criteria.field(%2$s::get%3$s).%4$s(StatusRecord.valueOf(%1$sQuery.get%3$s())));}", persistenceObjectName, persistenceClassName, upperPropertyName, SearchType.EQUAL.getCondition()));
                continue;
            }
            //树型结构后台管理 默认按parentId搜索 不提供其他字段搜索
            if (columnDef.getPropertyName().equals("parentId") && tableConfig.getIsTree()) {
                conditionList.add(String.format("if(%1$sQuery.get%3$s()!=null&&%1$sQuery.get%3$s()>=0) {booleanCriteria.and(Criteria.field(%2$s::get%3$s).%4$s(%1$sQuery.get%3$s()));}", persistenceObjectName, persistenceClassName, upperPropertyName, SearchType.EQUAL.getCondition()));
                continue;
            }
            if (columnDef.getShowInSearch()) {
                SearchType searchType = SearchType.getById(columnDef.getSearchType());
                if (searchType == null) {
                    searchType = SearchType.EQUAL;
                }
                queryFields.add(String.format("private %1$s %2$s;", field.getType().getSimpleName(), field.getPropertyName()));
                if (DatasourceType.isList(columnDef.getDatasourceType())) {
                    conditionList.add(String.format("if(%1$sQuery.get%3$s()!=null&&%1$sQuery.get%3$s()>=0) {booleanCriteria.and(Criteria.field(%2$s::get%3$s).%4$s(%1$sQuery.get%3$s()));}", persistenceObjectName, persistenceClassName, upperPropertyName, SearchType.EQUAL.getCondition()));
                    continue;
                }
                criteriaList.add(String.format("Criteria.field(%1$s::get%2$s).%4$s(%3$sQuery.get%2$s()))", persistenceClassName, upperPropertyName, persistenceObjectName, searchType.getCondition()));
            }
        }
        if (tableConfig.getTableName().equals("t_table_config")) {
            queryFields.add("private Long projectId;");
        }
        if (tableConfig.getOnlyAccessSelf()) {
            criteriaList.add(String.format("Criteria.field(%1$s::getCreateUserId).equal(SessionContext.getLoginUser().getUserId()))", persistenceClassName));
        }

        placeHolder.put(PlaceholderKey.$search_fields.name(), String.join("\n", queryFields));
        StringBuilder daoCondition = new StringBuilder();
        if (!CollectionsUtility.isNullOrEmpty(criteriaList)) {
            daoCondition.append(String.join(".and(", criteriaList)).append(";");
        }
        if (!CollectionsUtility.isNullOrEmpty(conditionList)) {
            for (String condition : conditionList) {
                daoCondition.append(condition);
            }
        }
        if (!StringUtility.isNullOrEmpty(daoCondition.toString())) {
            placeHolder.put(PlaceholderKey.$search_dao_condition.name(), daoCondition.insert(0, "BooleanCriteria booleanCriteria= BooleanCriteria.criteria(").append(" return booleanCriteria;").toString());
        } else {
            placeHolder.put(PlaceholderKey.$search_dao_condition.name(), "return null;");
        }
    }
}
