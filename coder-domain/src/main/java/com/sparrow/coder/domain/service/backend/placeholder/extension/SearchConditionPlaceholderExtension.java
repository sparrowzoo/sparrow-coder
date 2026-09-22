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

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
import com.sparrow.coder.domain.bo.ColumnDef;
import com.sparrow.coder.domain.bo.TableConfigBO;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.PlaceholderKey;
import com.sparrow.coder.enums.SearchType;
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
        List<String> daoCriteriaList = new ArrayList<>();
        TableConfigBO tableConfig = tableContext.getTableConfig();
        String statusCondition = "";

        for (ColumnDef columnDef : columnDefs) {
            Field field = fields.get(columnDef.getPropertyName());
            if (field == null) {
                log.error("field not found: " + columnDef.getPropertyName());
                continue;
            }
            String upperPropertyName = StringUtility.setFirstByteUpperCase(field.getPropertyName());

            if (columnDef.getShowInSearch()) {
                queryFields.add(String.format("private %1$s %2$s;", field.getType().getSimpleName(), field.getPropertyName()));
                SearchType searchType = SearchType.getById(columnDef.getSearchType());
                if (searchType == null) {
                    searchType = SearchType.EQUAL;
                }
                daoCriteriaList.add(String.format("Criteria.field(%1$s::get%2$s).%4$s(%3$sQuery.get%2$s()))", persistenceClassName, upperPropertyName, persistenceObjectName, searchType.getCondition()));
            }
            if (field.getType().equals(StatusRecord.class)) {
                queryFields.add(String.format("private Integer %1$s;", field.getPropertyName()));
                statusCondition = String.format("if(%1$sQuery.getStatus()!=null&&%1$sQuery.getStatus()>=0) {booleanCriteria.and(Criteria.field(%2$s::get%3$s).%4$s(StatusRecord.valueOf(%1$sQuery.get%3$s())));}", persistenceObjectName, persistenceClassName, upperPropertyName, SearchType.EQUAL.getCondition());
            }
        }
        if (tableConfig.getTableName().equals("t_table_config")) {
            queryFields.add("private Long projectId;");
        }
        if (tableConfig.getOnlyAccessSelf()) {
            daoCriteriaList.add(String.format("Criteria.field(%1$s::getCreateUserId).equal(SessionContext.getLoginUser().getUserId()))", persistenceClassName));
        }

        placeHolder.put(PlaceholderKey.$search_fields.name(), String.join("\n", queryFields));
        StringBuilder daoCondition = new StringBuilder();
        if (!CollectionsUtility.isNullOrEmpty(daoCriteriaList)) {
            daoCondition.append(String.join(".and(", daoCriteriaList)).append(";");
        }
        if (!StringUtility.isNullOrEmpty(statusCondition)) {
            daoCondition.append(statusCondition);
        }
        if (!StringUtility.isNullOrEmpty(daoCondition.toString())) {
            placeHolder.put(PlaceholderKey.$search_dao_condition.name(), daoCondition.insert(0,"BooleanCriteria booleanCriteria= BooleanCriteria.criteria(").append(" return booleanCriteria;").toString());
        } else {
            placeHolder.put(PlaceholderKey.$search_dao_condition.name(), "return null;");
        }
    }
}
