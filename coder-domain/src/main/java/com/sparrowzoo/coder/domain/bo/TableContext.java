package com.sparrowzoo.coder.domain.bo;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.validate.DigitalValidator;
import com.sparrowzoo.coder.domain.bo.validate.NoneValidator;
import com.sparrowzoo.coder.domain.bo.validate.RegexValidator;
import com.sparrowzoo.coder.domain.bo.validate.StringValidator;
import com.sparrowzoo.coder.domain.service.backend.ClassGenerator;
import com.sparrowzoo.coder.domain.service.backend.ClassPlaceholderGenerator;
import com.sparrowzoo.coder.domain.service.backend.DefaultClassGenerator;
import com.sparrowzoo.coder.domain.service.backend.DefaultClassPlaceholder;
import com.sparrowzoo.coder.domain.service.frontend.DefaultFrontendGenerator;
import com.sparrowzoo.coder.domain.service.frontend.DefaultFrontendPlaceholder;
import com.sparrowzoo.coder.domain.service.frontend.FrontendGenerator;
import com.sparrowzoo.coder.domain.service.frontend.FrontendPlaceholderGenerator;
import com.sparrowzoo.coder.enums.*;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class TableContext {
    private ProjectBO project;
    private Map<String, String> placeHolder;
    private TableConfigBO tableConfig;
    private EntityManager entityManager;
    private FrontendPlaceholderGenerator frontendPlaceholderGenerator;
    private FrontendGenerator frontendGenerator;
    private ClassPlaceholderGenerator classPlaceholderGenerator;
    private ClassGenerator classGenerator;
    private Map<String, Object> i18nMap = new HashMap<>();
    private Json json = JsonFactory.getProvider();


    public TableContext(TableConfigBO tableConfig, ProjectBO project) {
        this.placeHolder = new HashMap<>();
        this.project = project;
        this.tableConfig = tableConfig;
        try {
            this.entityManager = new SparrowEntityManager(Class.forName(tableConfig.getClassName()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.initErrorMessage();
        this.classPlaceholderGenerator = new DefaultClassPlaceholder(project, this);
        this.classGenerator = new DefaultClassGenerator(project, this);
        this.frontendPlaceholderGenerator = new DefaultFrontendPlaceholder(project, this);
        this.frontendGenerator = new DefaultFrontendGenerator(project, this);
    }

    public void initErrorMessage() {
        i18nMap.putIfAbsent("ErrorMessage", new HashMap<>());
        Map<String, String> errorMessages = (Map<String, String>) i18nMap.get("ErrorMessage");
        errorMessages.put(SparrowError.SYSTEM_SERVER_ERROR.name().toLowerCase(), "系统错误，请稍侯再试...");
        errorMessages.put(SparrowError.GLOBAL_PARAMETER_NULL.name().toLowerCase(), "参数不能为空...");
    }

    public Map<String, String> getValidateI18nMap(String propertyName) {
        i18nMap.putIfAbsent("validate", new HashMap<>());
        Map<String, Object> i18nMessages = (Map<String, Object>) i18nMap.get("validate");
        i18nMessages.putIfAbsent(propertyName, new HashMap<>());
        return (Map<String, String>) i18nMessages.get(propertyName);
    }

    public String getPoPackage() {
        String className = tableConfig.getClassName();
        return className.substring(0, className.lastIndexOf("."));
    }

    private String parseChineseName(String comment) {
        int commentIndex = comment.toLowerCase().indexOf("comment");
        if (commentIndex < 0) {
            return "";
        }
        String chineseName = comment.substring(commentIndex + 8);
        if (StringUtility.isNullOrEmpty(chineseName)) {
            return "";
        }
        return chineseName.replace("\"", "").replace("'", "");
    }


    public DigitalValidator generateDefaultNumberValidator(String propertyName, Boolean allowEmpty) {
        DigitalValidator validator = new DigitalValidator();
        validator.setAllowEmpty(allowEmpty);
        validator.setI18n(false);
        validator.setMinValue(null);
        validator.setMaxValue(null);
        validator.setCategory(DigitalCategory.INTEGER);
        validator.setPropertyName(propertyName);
        return validator;
    }

    public StringValidator generateDefaultStringValidator(String propertyName) {
        StringValidator validator = new StringValidator();
        validator.setAllowEmpty(false);
        validator.setI18n(false);
        validator.setMinLength(null);
        validator.setMaxLength(null);
        validator.setPropertyName(propertyName);
        return validator;
    }

    public List<ColumnDef> getColumns() {
        String columnConfigs = this.getTableConfig().getColumnConfigs();
        if (StringUtility.isNullOrEmpty(columnConfigs)) {
            return getDefaultColumns();
        }

        List<ColumnDef> columnDefs = new ArrayList<>();
        List<Object> jsonObjects = json.parseArray(columnConfigs);
        for (Object jsonObject : jsonObjects) {
            ColumnDef columnDef = json.toJavaObject(jsonObject, ColumnDef.class);
            columnDefs.add(columnDef);
            Object jsonValidator = json.getJSONObject(jsonObject, "validator");
            this.parseValidator(columnDef, jsonValidator);
        }
        return columnDefs;
    }

    public List<ColumnDef> getOriginalColumns() {
        return this.getColumns().stream().filter(c -> c.getColumnType().equals(ColumnType.NORMAL.getIdentity())).collect(Collectors.toList());
    }

    public void parseValidator(ColumnDef columnDef, Object jsonValidator) {
        String validateType = columnDef.getValidateType();
        if (StringUtility.isNullOrEmpty(validateType) || validateType.startsWith("nullable")) {
            NoneValidator noneValidator = new NoneValidator(columnDef.getJavaType());
            noneValidator.setClazz(columnDef.getJavaType());
            columnDef.setValidator(noneValidator);
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

    public List<ColumnDef> getDefaultColumns() {
        String tableClassName = this.getEntityManager().getSimpleClassName();
        List<ColumnDef> columnDefs = new ArrayList<>();
        Map<String, Field> fieldMap = this.getEntityManager().getPropertyFieldMap();
        int i = 0;
        for (String propertyName : fieldMap.keySet()) {
            Field field = fieldMap.get(propertyName);
            ColumnDef columnDef = new ColumnDef();
            columnDef.setTableClassName(tableClassName);
            columnDef.setPropertyName(propertyName);
            columnDef.setChineseName(this.parseChineseName(field.getColumnDefinition()));
            columnDef.setSubsidiaryColumns("");
            columnDef.setJavaType(field.getType().getName());
            columnDef.setEnableHidden(true);
            columnDef.setDefaultHidden(false);
            if (entityManager.getPoPropertyNames() != null && entityManager.getPoPropertyNames().contains(field.getPropertyName())) {
                columnDef.setShowInEdit(false);
                columnDef.setReadOnly(true);
            } else {
                columnDef.setShowInEdit(true);
            }
            columnDef.setShowInList(true);
            columnDef.setShowInSearch(false);
            columnDef.setAllowNull(false);
            columnDef.setPlaceholder("");
            columnDef.setDefaultValue("");
            columnDef.setSearchType(SearchType.EQUAL.getIdentity());
            columnDef.setValidateType(null);
            columnDef.setValidator(new NoneValidator(columnDef.getJavaType()));
            columnDef.setDataSourceType(DataSourceType.NULL.getIdentity());
            columnDef.setDataSourceParams("");
            if (field.getJoinTable() != null) {
                columnDef.setDataSourceType(DataSourceType.API.getIdentity());
                columnDef.setDataSourceParams(field.getJoinTable().name());
            }
            columnDef.setColumnType(ColumnType.NORMAL.getIdentity());
            columnDef.setHeaderType(HeaderType.NORMAL.getIdentity());
            columnDef.setCellType(CellType.NORMAL.getIdentity());
            if (entityManager.getPrimary() != null && columnDef.getPropertyName().equals(entityManager.getPrimary().getPropertyName())) {
                columnDef.setControlType(ControlType.INPUT_HIDDEN.getIdentity());
                columnDef.setValidateType("digital");
                columnDef.setValidator(this.generateDefaultNumberValidator(columnDef.getPropertyName(), true));
            } else {
                if (columnDef.isNumber()) {
                    columnDef.setValidateType("digital");
                    columnDef.setValidator(this.generateDefaultNumberValidator(columnDef.getPropertyName(), false));
                } else if (!columnDef.getAllowNull() && columnDef.getJavaType().equals(String.class.getName())) {
                    columnDef.setValidateType("string");
                    columnDef.setValidator(this.generateDefaultStringValidator(columnDef.getPropertyName()));
                }
                columnDef.setControlType(JavaTypeController.getByJavaType(columnDef.getJavaType()).getControlTypes()[0].getIdentity());
            }
            columnDef.setSort(i++);
            columnDefs.add(columnDef);
        }
        TableConfigBO tableConfig = this.getTableConfig();
        if (tableConfig.getColumnFilter() >= 0) {
            columnDefs.add(ColumnDef.createFilter(tableClassName, tableConfig.getColumnFilter()));
        }
        if (tableConfig.getCheckable() >= 0) {
            columnDefs.add(ColumnDef.createCheckBox(tableClassName, tableConfig.getCheckable()));
        }
        if (tableConfig.getRowMenu() >= 0) {
            columnDefs.add(ColumnDef.createRowMenu(tableClassName, tableConfig.getRowMenu()));
        }
        columnDefs.sort(Comparator.comparingInt(ColumnDef::getSort));
        return columnDefs;
    }
}
