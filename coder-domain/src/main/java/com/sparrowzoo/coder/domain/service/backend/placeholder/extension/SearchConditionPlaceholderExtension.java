package com.sparrowzoo.coder.domain.service.backend.placeholder.extension;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.PlaceholderKey;
import com.sparrowzoo.coder.enums.SearchType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SearchConditionPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();

        EntityManager entityManager = tableContext.getEntityManager();
        String persistenceClassName = entityManager.getSimpleClassName();
        Map<String, Field> fields = entityManager.getPropertyFieldMap();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);
        List<ColumnDef> columnDefs = tableContext.getColumns();
        List<String> queryFields = new ArrayList<>();
        List<String> daoCriteriaList = new ArrayList<>();
        String statusCondition = "";

        for (ColumnDef columnDef : columnDefs) {
            Field field = fields.get(columnDef.getPropertyName());
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
                statusCondition = String.format("if(%1$sQuery.getStatus()!=null&&%1$sQuery.getStatus()>=0) {booleanCriteria.and(Criteria.field(%2$s::get%3$s).%4$s(StatusRecord.valueOf(%3$sQuery.get%3$s())));}", persistenceObjectName, persistenceClassName, upperPropertyName, SearchType.EQUAL.getCondition());
            }
        }
        if (tableContext.getTableConfig().getOnlyAccessSelf()) {
            daoCriteriaList.add(String.format("Criteria.field(%1$s::createUserId).equal(ThreadContext.getLoginToken().getUserId())", persistenceClassName));
        }

        placeHolder.put(PlaceholderKey.$search_fields.name(), String.join("\n", queryFields));

        StringBuilder daoCondition = new StringBuilder();
        if (!CollectionsUtility.isNullOrEmpty(daoCriteriaList)) {
            daoCondition.append(String.format("BooleanCriteria booleanCriteria= BooleanCriteria.criteria(%1$s", String.join(".and(", daoCriteriaList)));
        }
        if (!StringUtility.isNullOrEmpty(statusCondition)) {
            daoCondition.append(statusCondition);
        }
        if (StringUtility.isNullOrEmpty(daoCondition.toString())) {
            placeHolder.put(PlaceholderKey.$search_dao_condition.name(), daoCondition.append("return booleanCriteria;").toString());
        } else {
            placeHolder.put(PlaceholderKey.$search_dao_condition.name(), "return null");
        }
    }
}
