package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrow.core.Pair;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.json.Json;
import com.sparrow.orm.Field;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.FileUtility;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.bo.validate.*;
import com.sparrowzoo.coder.domain.service.ArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.ValidatorMessageGenerator;
import com.sparrowzoo.coder.domain.service.frontend.generator.column.ColumnGenerator;
import com.sparrowzoo.coder.domain.service.registry.ColumnGeneratorRegistry;
import com.sparrowzoo.coder.domain.service.registry.ValidatorRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.enums.ColumnType;
import com.sparrowzoo.coder.enums.FrontendKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import java.io.File;
import java.util.*;

public class DefaultFrontendPlaceholder implements FrontendPlaceholderGenerator {
    protected final ProjectBO project;
    protected final TableContext tableContext;
    protected final ColumnGenerator columnGenerator;
    protected List<ColumnDef> columnDefs;
    protected Json json = JsonFactory.getProvider();

    protected Map<String, ValidatorMessageGenerator> validateContainer;

    public DefaultFrontendPlaceholder(ProjectBO project, TableContext tableContext) {
        this.project = project;
        this.tableContext = tableContext;
        this.columnDefs = tableContext.getColumns();
        ColumnGeneratorRegistry columnGeneratorRegistry = ColumnGeneratorRegistry.getInstance();
        ArchitectureGenerator architectureGenerator = this.project.getArchitecture(ArchitectureCategory.FRONTEND);
        String architectureName = architectureGenerator.getName();
        this.columnGenerator = columnGeneratorRegistry.getObject(architectureName);
        validateContainer = ValidatorRegistry.getInstance().getRegistry().get(architectureName);
        this.init();
    }

    @Override
    public String getPath(FrontendKey key) {
        String originPath = this.project.getScaffoldConfig().getProperty(key.name().toLowerCase());
        String persistenceObjectByDot = tableContext.getPlaceHolder().get(PlaceholderKey.$persistence_object_by_horizontal.name());
        originPath = originPath.replace(PlaceholderKey.$persistence_object_by_horizontal.name(), persistenceObjectByDot);
        originPath = originPath.replace(PlaceholderKey.$persistence_class_name.name(), tableContext.getEntityManager().getSimpleClassName());
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(originPath);
        return new FileNameBuilder(fileNameProperty.getName().replace(".", File.separator)).extension(fileNameProperty.getExtension()).build();
    }

    public void init() {
        Map<String, String> placeholder = tableContext.getPlaceHolder();
        placeholder.put(PlaceholderKey.$frontend_path_page.name(), this.getPath(FrontendKey.PAGE));
        placeholder.put(PlaceholderKey.$frontend_path_api.name(), this.getPath(FrontendKey.API));
        placeholder.put(PlaceholderKey.$frontend_path_add.name(), this.getPath(FrontendKey.ADD));
        placeholder.put(PlaceholderKey.$frontend_path_edit.name(), this.getPath(FrontendKey.EDIT));
        placeholder.put(PlaceholderKey.$frontend_path_search.name(), this.getPath(FrontendKey.SEARCH));
        placeholder.put(PlaceholderKey.$frontend_path_columns.name(), this.getPath(FrontendKey.COLUMNS));
        placeholder.put(PlaceholderKey.$frontend_path_schema.name(), this.getPath(FrontendKey.SCHEMA));
        placeholder.put(PlaceholderKey.$frontend_path_operation.name(), this.getPath(FrontendKey.OPERATION));

        placeholder.put(PlaceholderKey.$frontend_class.name(), this.generateClass());
        placeholder.put(PlaceholderKey.$frontend_column_filter.name(), tableContext.getTableConfig().getColumnFilter().toString());
        this.generatorColumns();
        placeholder.put(PlaceholderKey.$frontend_column_import.name(), this.generateImport());
        placeholder.put(PlaceholderKey.$frontend_schema.name(), this.generateSchema());
    }


    private String generateClass() {
        Map<String, Field> fields = tableContext.getEntityManager().getPropertyFieldMap();
        StringBuilder fieldBuild = new StringBuilder();
        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            String property = String.format("%s:%s; \n", field.getPropertyName(), this.toType(fieldClazz));
            fieldBuild.append(property);
        }
        String className = tableContext.getTableConfig().getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);

        return String.format("export interface %1$s extends BasicData<%1$s> \n{\n %2$s\n}", className, fieldBuild);
    }

    private String generateImport() {
        if (CollectionsUtility.isNullOrEmpty(this.columnDefs)) {
            return "";
        }
        HashMap<String, String> imports = new HashMap<>();
        for (ColumnDef columnDef : this.columnDefs) {
            if (columnDef.getHeaderType() != null && !imports.containsKey(columnDef.getHeaderType().getComponentName())) {
                imports.put(columnDef.getHeaderType().getComponentName(), this.columnGenerator.importHeader(columnDef.getHeaderType()));
            }
            if (columnDef.getCellType() != null && !imports.containsKey(columnDef.getCellType().getComponentName())) {
                imports.put(columnDef.getCellType().getComponentName(), this.columnGenerator.importCell(columnDef.getCellType()));
            }
        }
        return String.join("\n", imports.values());
    }


    private String generateSchema() {
        if (CollectionsUtility.isNullOrEmpty(this.columnDefs)) {
            return "";
        }
        List<String> schemas = new ArrayList<>();
        ProjectConfigBO projectConfig = project.getProjectConfig();
        for (ColumnDef columnDef : this.columnDefs) {
            if (!columnDef.getShowInEdit()) {
                continue;
            }
            if (columnDef.getColumnType().equals(ColumnType.ACTION) ||
                    columnDef.getColumnType().equals(ColumnType.CHECK) ||
                    columnDef.getColumnType().equals(ColumnType.FILTER)
            ) {
                continue;
            }
            String validateType = columnDef.getValidateType() == null ? "nullableValidatorMessageGenerator" : columnDef.getValidateType();
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

    private void generatorColumns() {
        if (CollectionsUtility.isNullOrEmpty(this.columnDefs)) {
            return;
        }
        Map<String,String> placeholder=this.tableContext.getPlaceHolder();
        List<String> columns = new ArrayList<>();
        List<String> addFormItems = new ArrayList<>();
        List<String> editFormItems = new ArrayList<>();

        Map<String, Object> columnI18nMap = tableContext.getI18nMap();
        for (ColumnDef columnDef : this.columnDefs) {
            if (columnDef.getShowInList()) {
                columns.add(columnGenerator.column(columnDef));
            }
            if (columnDef.getShowInEdit()) {
                addFormItems.add(columnGenerator.edit(columnDef,true));
                editFormItems.add(columnGenerator.edit(columnDef,false));
            }
            columnI18nMap.put(columnDef.getPropertyName(), columnDef.getChineseName());
        }
        String columnStr = String.join(",", columns);
        String addFormItemStr = String.join("\n", addFormItems);
        String editFormItemStr=String.join("\n",editFormItems);
        String className = tableContext.getEntityManager().getSimpleClassName();
        String columnDefs = String.format("export const columns: ColumnDef<%1$s>[] = [\n%2$s\n];", className, columnStr);
        placeholder.put(PlaceholderKey.$frontend_column_defs.name(),columnDefs);
        placeholder.put(PlaceholderKey.$frontend_add_form_items.name(),addFormItemStr);
        placeholder.put(PlaceholderKey.$frontend_edit_form_items.name(),editFormItemStr);

    }

    private String toType(Class<?> clazz) {
        if (Number.class.isAssignableFrom(clazz)) {
            return "number";
        }
        if (Boolean.class.isAssignableFrom(clazz)) {
            return "boolean";
        }
        return "string";
    }
}
