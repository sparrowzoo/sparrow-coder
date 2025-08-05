package com.sparrowzoo.coder.domain.service.frontend.placeholder.extension;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.protocol.dao.enums.ListDatasourceType;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.bo.validate.Validator;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
public class ReactColumnPlaceholderExtension extends AbstractPlaceholderExtension {
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
        placeholder.put(PlaceholderKey.$frontend_column_import.name(), this.generateImport(tableContext, columnGenerator, project));
        placeholder.put(PlaceholderKey.$frontend_schema.name(), this.generateSchema(tableContext, project, validateContainer));
    }

    private String generateImport(TableContext tableContext, ColumnGenerator columnGenerator, ProjectBO project) {
        List<ColumnDef> columnDefs = tableContext.getColumns();
        HashMap<String, String> imports = new HashMap<>();
        for (ColumnDef columnDef : columnDefs) {
            if (columnDef.getHeaderType() != null) {
                HeaderType headerType = HeaderType.getById(columnDef.getHeaderType());
                if (headerType != null && !imports.containsKey(headerType.getComponentName())) {
                    imports.put(headerType.getComponentName(), columnGenerator.importHeader(headerType, project));
                }
            }

            if (columnDef.getCellType() != null) {
                CellType cellType = CellType.getById(columnDef.getCellType());
                if (cellType != null && !imports.containsKey(cellType.getComponentName())) {
                    imports.put(cellType.getComponentName(), columnGenerator.importCell(cellType, project));
                }
            }
        }
        return String.join("\n", imports.values());
    }

    private void generatorColumns(TableContext tableContext, ColumnGenerator columnGenerator, ProjectBO project) {
        List<ColumnDef> columnDefs = tableContext.getColumns();
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        List<String> columns = new ArrayList<>();
        List<String> addFormItems = new ArrayList<>();
        List<String> editFormItems = new ArrayList<>();
        List<String> frontSearchItems = new ArrayList<>();
        List<String> frontQueryFields = new ArrayList<>();
        EntityManager entityManager = tableContext.getEntityManager();
        String persistenceClassName = entityManager.getSimpleClassName();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);
        Map<String, Field> fields = entityManager.getPropertyFieldMap();

        Map<String, Object> columnI18nMap = tableContext.getI18nMap();
        for (ColumnDef columnDef : columnDefs) {
            Field field = fields.get(columnDef.getPropertyName());

            if (columnDef.getShowInList()) {
                columns.add(columnGenerator.column(columnDef, project));
            }


            if (columnDef.getShowInEdit()) {
                addFormItems.add(columnGenerator.edit(columnDef, project, true));
                editFormItems.add(columnGenerator.edit(columnDef, project, false));
            }
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
        String columnStr = String.join(",", columns);
        String addFormItemStr = String.join("\n", addFormItems);
        String editFormItemStr = String.join("\n", editFormItems);
        String className = tableContext.getEntityManager().getSimpleClassName();
        String frontColumnDefs = String.format("export const columns: ColumnDef<%1$s>[] = [\n%2$s\n];", className, columnStr);
        placeholder.put(PlaceholderKey.$frontend_column_defs.name(), frontColumnDefs);
        placeholder.put(PlaceholderKey.$frontend_add_form_items.name(), addFormItemStr);
        placeholder.put(PlaceholderKey.$frontend_edit_form_items.name(), editFormItemStr);
        placeholder.put(PlaceholderKey.$frontend_i18n_message.name(), JsonFactory.getProvider().toString(tableContext.getI18nMap()));
        placeholder.put(PlaceholderKey.$frontend_search_items.name(), String.join("\n", frontSearchItems));
        placeholder.put(PlaceholderKey.$frontend_query_fields.name(), String.join("\n", frontQueryFields));

    }

    private String generateSchema(TableContext tableContext, ProjectBO project, Map<String, ValidatorMessageGenerator> validateContainer) {
        List<ColumnDef> columnDefs = tableContext.getColumns();
        List<String> schemas = new ArrayList<>();
        ProjectConfigBO projectConfig = project.getProjectConfig();
        for (ColumnDef columnDef : columnDefs) {
            if (!columnDef.getShowInEdit()) {
                continue;
            }
            ColumnType columnType = ColumnType.getById(columnDef.getColumnType());
            if (ColumnType.ACTION.equals(columnType) ||
                    ColumnType.CHECK.equals(columnType) ||
                    ColumnType.FILTER.equals(columnType)
            ) {
                continue;
            }
            String validateType = columnDef.getValidateType() == null ? "nullable" : columnDef.getValidateType();
            ValidatorMessageGenerator messageGenerator = validateContainer.get(validateType);
            Validator validator = columnDef.getValidator();
            if (validator != null) {
                Map<String, String> columnI18nMap = tableContext.getValidateI18nMap(columnDef.getPropertyName());
                validator.setI18nConfig(columnI18nMap);
                validator.setPropertyName(columnDef.getPropertyName());
                validator.setI18n(projectConfig.getI18n());
            }
            schemas.add(messageGenerator.generateConfig(columnDef.getPropertyName(), validator));
        }
        return String.join(",", schemas);
    }
}
