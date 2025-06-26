package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.io.file.FileNameProperty;
import com.sparrow.json.Json;
import com.sparrow.orm.Field;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.bo.validate.*;
import com.sparrowzoo.coder.domain.service.ValidatorMessageGenerator;
import com.sparrowzoo.coder.domain.service.frontend.generator.column.ColumnGenerator;
import com.sparrowzoo.coder.domain.service.registry.ColumnGeneratorRegistry;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.domain.service.registry.ValidatorRegistry;
import com.sparrowzoo.coder.enums.FrontendKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import java.io.File;
import java.util.*;

public class DefaultFrontendPlaceholder implements FrontendPlaceholder {
    protected final TableConfigRegistry registry;
    protected final TableContext tableContext;
    protected final Properties config;
    protected final ColumnGenerator columnGenerator;
    protected List<ColumnDef> columnDefs;

    protected Json json = JsonFactory.getProvider();

    protected Map<String, ValidatorMessageGenerator> validateContainer;

    public DefaultFrontendPlaceholder(TableConfigRegistry registry, String tableName, String architecture) {
        this.registry = registry;
        this.config = registry.getProjectConfig();
        this.tableContext = registry.getTableContext(tableName);
        ColumnGeneratorRegistry columnGeneratorRegistry = ColumnGeneratorRegistry.getInstance();
        this.columnGenerator = columnGeneratorRegistry.getObject(architecture);
        String columnConfig = tableContext.getTableConfig().getColumnConfigs();
        if (!StringUtility.isNullOrEmpty(columnConfig)) {
            this.columnDefs = new ArrayList<>();
            List<Object> jsonObjects = json.parseArray(columnConfig);
            for (int i = 0; i < jsonObjects.size(); i++) {
                Object jsonObject = jsonObjects.get(i);
                ColumnDef columnDef = json.toJavaObject(jsonObject, ColumnDef.class);
                this.columnDefs.add(columnDef);
                Object jsonValidator = json.getJSONObject(jsonObject, "validator");
                this.parseValidator(registry, columnDef, jsonValidator);
            }
        }
        validateContainer = ValidatorRegistry.getInstance().getRegistry().get(architecture);
    }

    public void parseValidator(TableConfigRegistry registry, ColumnDef columnDef, Object jsonValidator) {
        String validateType = columnDef.getValidateType();
        if (validateType.startsWith("nullable")) {
            columnDef.setValidator(new NoneValidator());
            return;
        }
        if (validateType.startsWith("digital")) {
            DigitalValidator validatorString = json.toJavaObject(jsonValidator, DigitalValidator.class);
            columnDef.setValidator(validatorString);
            return;
        }
        if (validateType.startsWith("string")) {
            StringValidator validatorString = json.toJavaObject(jsonValidator, StringValidator.class);
            columnDef.setValidator(validatorString);
            return;
        }
        RegexValidator validatorString = json.toJavaObject(jsonValidator, RegexValidator.class);
        columnDef.setValidator(validatorString);
    }

    @Override
    public String getPath(FrontendKey key) {
        String originPath = this.config.getProperty(key.name().toLowerCase());
        String persistenceObjectByDot = tableContext.getPlaceHolder().get(PlaceholderKey.$persistence_object_by_horizontal.name());
        originPath = originPath.replace(PlaceholderKey.$persistence_object_by_horizontal.name(), persistenceObjectByDot);
        originPath = originPath.replace(PlaceholderKey.$persistence_class_name.name(), tableContext.getEntityManager().getSimpleClassName());
        FileNameProperty fileNameProperty = FileUtility.getInstance().getFileNameProperty(originPath);
        return new FileNameBuilder(fileNameProperty.getName().replace(".", File.separator)).extension(fileNameProperty.getExtension()).build();
    }

    @Override
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
        placeholder.put(PlaceholderKey.$frontend_column_defs.name(), this.generatorColumns());
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
            if (!imports.containsKey(columnDef.getHeaderType().getComponentName())) {
                imports.put(columnDef.getHeaderType().getComponentName(), this.columnGenerator.importHeader(columnDef.getHeaderType()));
            }
            if (!imports.containsKey(columnDef.getCellType().getComponentName())) {
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
        ProjectConfigBO projectConfig = registry.getProject();
        for (ColumnDef columnDef : this.columnDefs) {
            if (!columnDef.getShowInEdit()) {
                continue;
            }
            ValidatorMessageGenerator messageGenerator = validateContainer.get(columnDef.getValidateType());
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

    private String generatorColumns() {
        if (CollectionsUtility.isNullOrEmpty(this.columnDefs)) {
            return "";
        }
        List<String> columns = new ArrayList<>();
        Map<String, Object> columnI18nMap = tableContext.getI18nMap();
        for (ColumnDef columnDef : this.columnDefs) {
            if (!columnDef.getShowInList()) {
                continue;
            }
            columns.add(columnGenerator.column(columnDef));
            columnI18nMap.put(columnDef.getPropertyName(), columnDef.getChineseName());
        }
        String columnStr = String.join(",", columns);
        String className = tableContext.getTableConfig().getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        return String.format("export const columns: ColumnDef<%1$s>[] = [\n%2$s\n];", className, columnStr);
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
