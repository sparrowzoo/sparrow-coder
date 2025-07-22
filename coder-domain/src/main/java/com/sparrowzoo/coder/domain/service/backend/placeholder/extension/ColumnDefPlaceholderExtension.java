package com.sparrowzoo.coder.domain.service.backend.placeholder.extension;

import com.sparrowzoo.coder.constant.EnumNames;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.DatasourceType;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class ColumnDefPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        List<ColumnDef> columnDefs = tableContext.getColumns();
        List<String> dictionaries = new ArrayList<>();

        boolean hasBusinessEnum = false;
        for (ColumnDef columnDef : columnDefs) {
            if (DatasourceType.ENUM.getIdentity().equals(columnDef.getDatasourceType())) {
                hasBusinessEnum = true;
            }
            if (DatasourceType.TABLE.getIdentity().equals(columnDef.getDatasourceType())) {
                //与@JoinTable(name = "t_project_config") 功能相
            }
        }

        if (tableContext.getTableConfig().getTableName().equals("t_table_config")) {
            dictionaries.add(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.CELL_TYPE));
            dictionaries.add(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.DATASOURCE_TYPE));
            dictionaries.add(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.COLUMN_TYPE));
            dictionaries.add(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.CONTROL_TYPE));
            dictionaries.add(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.HEADER_TYPE));
            dictionaries.add(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.SEARCH_TYPE));
            dictionaries.add("pagerResult.putDictionary(\"validateType\", ValidatorRegistry.getInstance().getValidatorNames(\"react\"));");
            placeHolder.put(PlaceholderKey.$enum_container_inject.name(), "@Inject\n" +
                    "    private EnumsContainer coderEnumsContainer;");
        }

        if (dictionaries.size() > 0) {
            placeHolder.put(PlaceholderKey.$dictionaries.name(), String.join("\n", dictionaries));
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
