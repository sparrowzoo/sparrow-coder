package com.sparrowzoo.coder.domain.service.backend.placeholder.extension;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.Map;

@Named
public class FieldsPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        tableContext.getColumns();
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
        if (isAssignableDisplayText) {
            fieldBuild.append("private String displayText; \n");
            placeHolder.put(PlaceholderKey.$get_sets_display_text.name(), String.format("%1$sBO.setDisplayText(%1$s.getDisplayText());", persistenceObjectName));
            placeHolder.put(PlaceholderKey.$assemble_kvs.name(), getKvsAssemblePlaceHolder(tableContext));
            placeHolder.put(PlaceholderKey.$adapter_kvs.name(), getAdapterPlaceHolder(tableContext));
        } else {
            placeHolder.put(PlaceholderKey.$get_sets_display_text.name(), "");
            placeHolder.put(PlaceholderKey.$assemble_kvs.name(), "");
            placeHolder.put(PlaceholderKey.$adapter_kvs.name(), "");
        }
        fieldBuild.append("}");
        placeHolder.put(PlaceholderKey.$get_sets.name(), fieldBuild.toString());
        placeHolder.put(PlaceholderKey.$get_sets_params.name(), paramFieldBuild.toString());
    }

    private String getKvsAssemblePlaceHolder(TableContext tableContext) {
        EntityManager entityManager = tableContext.getEntityManager();
        String persistenceClassName = entityManager.getSimpleClassName();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);

        return String.format("\npublic List<KeyValue<%3$s, String>> get%1$sKvs(ListRecordTotalBO<%1$sBO> %2$sListTotalRecord){\n" +
                        "            List<%1$sBO> %2$sBOList = %2$sListTotalRecord.getList();\n" +
                        "            List<KeyValue<%3$s, String>> %2$sKvs = new ArrayList<>(%2$sBOList.size());\n" +
                        "            for(%1$sBO %2$sBO : %2$sBOList){\n" +
                        "                %2$sKvs.add(new KeyValue<>(%2$sBO.getId(), %2$sBO.getDisplayText()));\n" +
                        "            }\n" +
                        "            return %2$sKvs;\n" +
                        "        }", persistenceClassName, persistenceObjectName
                , entityManager.getPrimary().getType().getSimpleName());
    }

    private String getAdapterPlaceHolder(TableContext tableContext) {

        EntityManager entityManager = tableContext.getEntityManager();
        String persistenceClassName = entityManager.getSimpleClassName();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);

        return String.format("@PostMapping(\"kvs.json\")\n" +
                        "@ApiOperation(\"Key-Value对列表\")\n" +
                        "public List<KeyValue<%3$s, String>> get%1$sKvs(){\n" +
                        "            %1$sQuery %2$sQuery = new %1$sQuery();\n" +
                        "            %2$sQuery.setStatus(StatusRecord.ENABLE.ordinal());\n" +
                        "            %2$sQuery.setPageSize(Integer.MAX_VALUE);\n" +
                        "            ListRecordTotalBO<%1$sBO> %2$ss= this.%2$sService.query%1$s(%2$sQuery);\n" +
                        "            return %2$sAssemble.get%1$sKvs(%2$ss);\n" +
                        "        }", persistenceClassName, persistenceObjectName,
                entityManager.getPrimary().getType().getSimpleName());
    }
}
