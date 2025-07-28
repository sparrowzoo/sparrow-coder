package com.sparrowzoo.coder.domain.service.backend.placeholder.extension;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
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
        String persistenceClassName = entityManager.getSimpleClassName();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);

        Map<String, Field> fields = entityManager.getPropertyFieldMap();
        boolean isAssignableDisplayText = entityManager.isAssignableFromDisplayText();
        String getSetPrefix = isAssignableDisplayText ? ",DisplayTextAccessor{" : "{";
        StringBuilder fieldBuild = new StringBuilder(getSetPrefix);
        StringBuilder paramFieldBuild = new StringBuilder();

        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            String property = String.format("private %s %s; \n", fieldClazz.getSimpleName(), field.getPropertyName());
            fieldBuild.append(property);
            if (entityManager.getPoPropertyNames() != null && !entityManager.getPoPropertyNames().contains(field.getPropertyName())) {
                paramFieldBuild.append(property);
            }
        }
        //如果实现了DisplayText 接口，则生成KVS的接口，提供给外表关联使用
        if (isAssignableDisplayText) {
            fieldBuild.append("private String displayText; \n");
            placeHolder.put(PlaceholderKey.$get_sets_display_text.name(), String.format("%1$sBO.setDisplayText(%1$s.getDisplayText());", persistenceObjectName));
            placeHolder.put(PlaceholderKey.$service_kvs.name(), getKVServicePlaceHolder(tableContext));
        } else {
            placeHolder.put(PlaceholderKey.$get_sets_display_text.name(), "");
            placeHolder.put(PlaceholderKey.$service_kvs.name(), "");
        }
        fieldBuild.append("}");
        if(entityManager.getStatus() != null) {
            placeHolder.put(PlaceholderKey.$status_field.name(), entityManager.getStatus().getPropertyName());
        }
        placeHolder.put(PlaceholderKey.$get_sets.name(), fieldBuild.toString());
        placeHolder.put(PlaceholderKey.$get_sets_params.name(), paramFieldBuild.toString());
    }

    private String getKVServicePlaceHolder(TableContext tableContext) {

        EntityManager entityManager = tableContext.getEntityManager();
        String persistenceClassName = entityManager.getSimpleClassName();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);

        return String.format("public List<KeyValue<Integer, String>> get%1$sKvs() {\n" +
                        "        %1$sQuery %2$sQuery = new %1$sQuery();\n" +
                        "        %2$sQuery.setStatus(StatusRecord.ENABLE.ordinal());\n" +
                        "        %2$sQuery.setPageSize(-1);\n" +
                        "        List<%1$sBO> %2$sBoList = this.%2$sRepository.query%1$ss(%2$sQuery);\n" +
                        "        List<KeyValue<Integer, String>> %2$sKvs = new ArrayList<>(%2$sBoList.size());\n" +
                        "        for (%1$sBO %2$sBO : %2$sBoList) {\n" +
                        "            %2$sKvs.add(new KeyValue<>(%2$sBO.getId().intValue(), %2$sBO.getDisplayText()));\n" +
                        "        }\n" +
                        "        return %2$sKvs;\n" +
                        "    }", persistenceClassName,
                persistenceObjectName);
    }
}
