package com.sparrowzoo.coder.domain.service.backend.placeholder.extension.dependency;

import com.sparrow.orm.Field;
import com.sparrow.protocol.dao.enums.ListDatasourceType;
import com.sparrowzoo.coder.constant.EnumNames;
import com.sparrowzoo.coder.domain.bo.CoderTriple;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
public class DictionaryPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        List<ColumnDef> columnDefs = tableContext.getColumns();
        CoderTriple dictionaryTriple = new CoderTriple();
        for (ColumnDef columnDef : columnDefs) {
            if (ListDatasourceType.ENUM.getIdentity().equals(columnDef.getDatasourceType())) {
                dictionaryTriple.inject("@Inject\nprivate EnumsContainer businessEnumsContainer;");
                dictionaryTriple.code(String.format("pagerResult.putDictionary(\"%1$s\",businessEnumsContainer.getEnums(\"%2$s\"));", columnDef.getPropertyName(), columnDef.getDatasourceParams()));
            }
            if (ListDatasourceType.TABLE.getIdentity().equals(columnDef.getDatasourceType())) {
                String joinTableName = columnDef.getDatasourceParams();
                TableContext joinTableContext = registry.getTableContext(joinTableName);
                String joinServicePackage = joinTableContext.getPlaceHolder().get(PlaceholderKey.$package_service.name());
                String serviceClass = joinTableContext.getPlaceHolder().get(PlaceholderKey.$class_service.name());
                String joinClassName = joinTableContext.getPlaceHolder().get(PlaceholderKey.$persistence_class_name.name());
                String objectName = joinTableContext.getPlaceHolder().get(PlaceholderKey.$persistence_object_name.name());
                String joinFieldName = columnDef.getPropertyName();
                dictionaryTriple.addImport(String.format("import %1$s.%2$s;\n", joinServicePackage, serviceClass));
                dictionaryTriple.inject(String.format(" @Inject\n private %1$s %2$sService;", serviceClass, objectName));
                dictionaryTriple.code(String.format("pagerResult.putDictionary(\"%1$s\",this.%2$sService.get%3$sKvs());\n", joinFieldName, objectName, joinClassName));
            }
            if(tableContext.getEntityManager().getStatus()!=null){
                dictionaryTriple.inject("@Inject\nprivate EnumsContainer coderEnumsContainer;");
                dictionaryTriple.code(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.STATUS_RECORD));
            }
        }

        if (tableContext.getTableConfig().getTableName().equals("t_table_config")) {
            dictionaryTriple.code(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.CELL_TYPE));
            dictionaryTriple.code(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.DATASOURCE_TYPE));
            dictionaryTriple.code(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.COLUMN_TYPE));
            dictionaryTriple.code(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.CONTROL_TYPE));
            dictionaryTriple.code(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.HEADER_TYPE));
            dictionaryTriple.code(String.format("pagerResult.putDictionary(\"%1$s\",coderEnumsContainer.getEnums(\"%1$s\"));", EnumNames.SEARCH_TYPE));
            dictionaryTriple.code("pagerResult.putDictionary(\"validateType\", ValidatorRegistry.getInstance().getValidatorNames(\"react\"));");
            dictionaryTriple.inject("@Inject\nprivate EnumsContainer coderEnumsContainer;");
        }
        placeHolder.put(PlaceholderKey.$dictionary_import.name(), dictionaryTriple.joinImports());
        placeHolder.put(PlaceholderKey.$dictionary_inject.name(), dictionaryTriple.joinInjects());
        placeHolder.put(PlaceholderKey.$dictionaries.name(), dictionaryTriple.joinCode());
    }
}
