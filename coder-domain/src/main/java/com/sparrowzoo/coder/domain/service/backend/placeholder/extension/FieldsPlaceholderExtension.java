package com.sparrowzoo.coder.domain.service.backend.placeholder.extension;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.Map;

@Named
public class FieldsPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        EntityManager entityManager = tableContext.getEntityManager();
        Map<String, Field> fields = entityManager.getPropertyFieldMap();
        boolean isAssignableDisplayText = entityManager.isAssignableFromDisplayText();
        String getSetPrefix = isAssignableDisplayText ? ",DisplayTextAccessor{" : "{";
        StringBuilder fieldBuild = new StringBuilder(getSetPrefix);
        StringBuilder fieldDtoBuild = new StringBuilder(getSetPrefix);
        StringBuilder paramFieldBuild = new StringBuilder();

        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            String property = String.format("private %s %s; \n", fieldClazz.getSimpleName(), field.getPropertyName());
            fieldBuild.append(property);
            if (field.getType().equals(StatusRecord.class)) {
                property = String.format("private Integer %s; \n", field.getPropertyName());
            }
            fieldDtoBuild.append(property);
            if (entityManager.getPoPropertyNames() != null && !entityManager.getPoPropertyNames().contains(field.getPropertyName())) {
                paramFieldBuild.append(property);
            }
        }
        //如果实现了DisplayText 接口，则生成KVS的接口，提供给外表关联使用
        if (isAssignableDisplayText) {
            fieldBuild.append("private String displayText; \n");
        }
        fieldBuild.append("}");
        fieldDtoBuild.append("}");
        if (entityManager.getStatus() != null) {
            placeHolder.put(PlaceholderKey.$status_field.name(), entityManager.getStatus().getPropertyName());
        }
        placeHolder.put(PlaceholderKey.$get_sets_dto.name(), fieldDtoBuild.toString());
        placeHolder.put(PlaceholderKey.$get_sets.name(), fieldBuild.toString());
        placeHolder.put(PlaceholderKey.$get_sets_params.name(), paramFieldBuild.toString());
    }
}
