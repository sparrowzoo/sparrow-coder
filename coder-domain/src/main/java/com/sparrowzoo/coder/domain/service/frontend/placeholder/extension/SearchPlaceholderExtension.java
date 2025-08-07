package com.sparrowzoo.coder.domain.service.frontend.placeholder.extension;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.protocol.dao.enums.ListDatasourceType;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.ArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.ValidatorMessageGenerator;
import com.sparrowzoo.coder.domain.service.frontend.generator.column.ColumnGenerator;
import com.sparrowzoo.coder.domain.service.registry.ColumnGeneratorRegistry;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.domain.service.registry.ValidatorRegistry;
import com.sparrowzoo.coder.enums.*;
import com.sparrowzoo.coder.utils.JavaTsTypeConverter;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class SearchPlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext, TableConfigRegistry registry) {
        ProjectBO project = tableContext.getProject();
        List<ColumnDef> columnDefs = tableContext.getColumns();
        if (CollectionsUtility.isNullOrEmpty(columnDefs)) {
            return;
        }
        ColumnGeneratorRegistry columnGeneratorRegistry = ColumnGeneratorRegistry.getInstance();
        ArchitectureGenerator architectureGenerator = project.getArchitecture(ArchitectureCategory.FRONTEND);
        String architectureName = architectureGenerator.getName();
        ColumnGenerator columnGenerator = columnGeneratorRegistry.getObject(architectureName);
        Map<String, ValidatorMessageGenerator> validateContainer = ValidatorRegistry.getInstance().getRegistry().get(architectureName);
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        this.generatorColumns(tableContext, columnGenerator, project);
    }


    private void generatorColumns(TableContext tableContext, ColumnGenerator columnGenerator, ProjectBO project) {
        List<ColumnDef> columnDefs = tableContext.getColumns();
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        List<String> frontSearchItems = new ArrayList<>();
        List<String> frontQueryFields = new ArrayList<>();
        EntityManager entityManager = tableContext.getEntityManager();
        String persistenceClassName = entityManager.getSimpleClassName();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);
        Map<String, Field> fields = entityManager.getPropertyFieldMap();

        Map<String, Object> columnI18nMap = tableContext.getI18nMap();
        for (ColumnDef columnDef : columnDefs) {
            Field field = fields.get(columnDef.getPropertyName());
            columnI18nMap.put(columnDef.getPropertyName(), columnDef.getChineseName());
            if (field == null) {
                continue;
            }
            if (columnDef.getShowInSearch()) {
                if (columnDef.getDatasourceType().equals(ListDatasourceType.ENUM.getIdentity())) {
                    frontQueryFields.add(String.format("%2$s: %1$s;", "number", field.getPropertyName()));
                    frontSearchItems.add(String.format("<SearchSelect propertyName={\"%1$s\"} pageTranslate={pageTranslate} setSearchCondition={set%2$sQuery} dictionary={meta.result.data.dictionary['%1$s']}/>", field.getPropertyName(), persistenceClassName));
                } else {
                    frontQueryFields.add(String.format("%2$s: %1$s;", JavaTsTypeConverter.toTsType(field.getType()), field.getPropertyName()));
                    frontSearchItems.add(String.format("<SearchInput value={%1$sQuery?.%2$s||\"\"} \n" + "propertyName={\"%2$s\"} pageTranslate={pageTranslate} \n" + "setSearchCondition={set%3$sQuery}/>", persistenceObjectName, field.getPropertyName(), persistenceClassName));
                }
            }

            if (field.getType().equals(StatusRecord.class)) {
                frontQueryFields.add(String.format("%2$s: %1$s;", "number", field.getPropertyName()));
                frontSearchItems.add(String.format("<SearchSelect propertyName={\"%1$s\"} pageTranslate={pageTranslate} setSearchCondition={set%2$sQuery} dictionary={meta.result.data.dictionary['%1$s']}/>", field.getPropertyName(), persistenceClassName));
            }
        }
        placeholder.put(PlaceholderKey.$frontend_search_items.name(), String.join("\n", frontSearchItems));
        placeholder.put(PlaceholderKey.$frontend_query_fields.name(), String.join("\n", frontQueryFields));
    }
}
