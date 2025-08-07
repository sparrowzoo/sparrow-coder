package com.sparrowzoo.coder.domain.service.backend.placeholder.extension;

import com.sparrow.orm.EntityManager;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.Map;

@Named
public class KVServiceExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        EntityManager entityManager = tableContext.getEntityManager();
        String persistenceClassName = entityManager.getSimpleClassName();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);
        boolean isAssignableDisplayText = entityManager.isAssignableFromDisplayText();
        //如果实现了DisplayText 接口，则生成KVS的接口，提供给外表关联使用
        if (isAssignableDisplayText) {
            placeHolder.put(PlaceholderKey.$set_display_text.name(), String.format("%1$sBO.setDisplayText(%1$s.getDisplayText());", persistenceObjectName));
            placeHolder.put(PlaceholderKey.$service_kvs.name(), getKVServicePlaceHolder(tableContext));
        } else {
            placeHolder.put(PlaceholderKey.$set_display_text.name(), "");
            placeHolder.put(PlaceholderKey.$service_kvs.name(), "");
        }
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
