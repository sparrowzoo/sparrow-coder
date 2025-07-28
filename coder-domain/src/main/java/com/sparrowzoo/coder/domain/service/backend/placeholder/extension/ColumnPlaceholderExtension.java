package com.sparrowzoo.coder.domain.service.backend.placeholder.extension;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
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
public class ColumnPlaceholderExtension extends AbstractPlaceholderExtension {
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

        for (ColumnDef columnDef : columnDefs) {
            if (columnDef.getShowInSearch()) {
                Field field = fields.get(columnDef.getPropertyName());
                queryFields.add(String.format("private %1$s %2$s;", field.getType().getSimpleName(), field.getPropertyName()));
                String upperPropertyName = StringUtility.setFirstByteUpperCase(field.getPropertyName());
                SearchType searchType = SearchType.getById(columnDef.getSearchType());
                if (searchType == null) {
                    searchType = SearchType.EQUAL;
                }
                daoCriteriaList.add(String.format("Criteria.field(%1$s::get%2$s).%4$s(%3$sQuery.get%2$s()))", persistenceClassName, upperPropertyName, persistenceObjectName, searchType.getCondition()));
            }
        }

        placeHolder.put(PlaceholderKey.$search_fields.name(), String.join("\n", queryFields));
        if (!CollectionsUtility.isNullOrEmpty(daoCriteriaList)) {
            placeHolder.put(PlaceholderKey.$search_dao_condition.name(), String.format("BooleanCriteria.criteria(%1$s", String.join(".and(", daoCriteriaList)));
        } else {
            placeHolder.put(PlaceholderKey.$search_dao_condition.name(), "null");
        }
    }
}
