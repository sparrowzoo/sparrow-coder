package com.sparrowzoo.coder.domain.service.backend.placeholder.extension;

import com.sparrowzoo.coder.constant.EnumNames;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.enums.DataSourceType;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class ColumnDefPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        List<ColumnDef> columnDefs = tableContext.getColumns();
        List<String> enumDictionaries = new ArrayList<>();

        boolean hasBusinessEnum = false;
        for (ColumnDef columnDef : columnDefs) {
            if (DataSourceType.ENUM.getIdentity().equals(columnDef.getDataSourceType())) {
                hasBusinessEnum = true;
                enumDictionaries.add(String.format("pagerResult.putDictionary(\"%1$s\",enumsContainer.getEnums(\"%1$s\"));", columnDef.getDataSourceParams()));
            }
        }

        if (tableContext.getTableConfig().getTableName().equals("t_table_config")) {
            enumDictionaries.add("pagerResult.putDictionary(EnumNames.CELL_TYPE,coderEnumsContainer.getEnums(EnumNames.CELL_TYPE));");
            enumDictionaries.add("pagerResult.putDictionary(EnumNames.HEADER_TYPE,coderEnumsContainer.getEnums(EnumNames.HEADER_TYPE));");
            enumDictionaries.add("pagerResult.putDictionary(EnumNames.DATASOURCE_TYPE,coderEnumsContainer.getEnums(EnumNames.DATASOURCE_TYPE));");
            enumDictionaries.add("pagerResult.putDictionary(EnumNames.COLUMN_TYPE,coderEnumsContainer.getEnums(EnumNames.COLUMN_TYPE));");
            placeHolder.put(PlaceholderKey.$enum_container_inject.name(), "@Inject\n" +
                    "    private EnumsContainer coderEnumsContainer;");
        }

        if (enumDictionaries.size() > 0) {
            placeHolder.put(PlaceholderKey.$dictionaries.name(), String.join("\n", enumDictionaries));
        } else {
            placeHolder.put(PlaceholderKey.$dictionaries.name(), "");
        }

        if (hasBusinessEnum) {
            placeHolder.put(PlaceholderKey.$enum_container_inject.name(), "@Inject\n" +
                    "    private EnumsContainer businessEnumsContainer;");
        }
        if (!placeHolder.containsKey(PlaceholderKey.$enum_container_inject.name())) {
            placeHolder.put(PlaceholderKey.$enum_container_inject.name(), "");
        }
    }
}
