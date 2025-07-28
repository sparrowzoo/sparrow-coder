package com.sparrowzoo.coder.moke.beans;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.ColumnDef;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.bo.validate.DigitalValidator;
import com.sparrowzoo.coder.domain.bo.validate.RegexValidator;
import com.sparrowzoo.coder.domain.bo.validate.StringValidator;
import com.sparrowzoo.coder.domain.bo.validate.Validator;
import com.sparrowzoo.coder.enums.*;
import com.sparrowzoo.coder.protocol.param.TableConfigParam;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;
import com.sparrowzoo.coder.repository.TableConfigRepository;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Named
public class MockTableConfigRepository implements TableConfigRepository {
    @Override
    public Long save(TableConfigParam tableConfigParam) {
        return null;
    }

    @Override
    public Integer delete(Set<Long> tableConfigIds) {
        return null;
    }

    @Override
    public Integer disable(Set<Long> tableConfigIds) {
        return null;
    }

    @Override
    public Integer enable(Set<Long> tableConfigIds) {
        return null;
    }

    private void getColumnDefs(TableContext tableContext) {
//
//        columnDefs.add(getColumnDef("id", tableClassName, "", ColumnType.CHECK, HeaderType.CHECK_BOX, CellType.CHECK_BOX, ControlType.INPUT_HIDDEN, DataSourceType.NULL, false, true,null,null));
//        columnDefs.add(getColumnDef("name", tableClassName, "项目名称", ColumnType.NORMAL, HeaderType.NORMAL_SORT_FILTER, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, true, true,"stringValidatorMessageGenerator",this.generateString("name")));
//        columnDefs.add(getColumnDef("frontendName", tableClassName, "前端项目名称", ColumnType.NORMAL, HeaderType.NORMAL_SORT, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, true, true,"stringValidatorMessageGenerator",this.generateString("frontendName")));
//        columnDefs.add(getColumnDef("chineseName", tableClassName, "项目中文名", ColumnType.NORMAL, HeaderType.NORMAL_FILTER, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, true, true,"stringValidatorMessageGenerator",this.generateString("chineseName")));
//        columnDefs.add(getColumnDef("description", tableClassName, "项目描述", ColumnType.NORMAL, HeaderType.NORMAL_FILTER, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, true, true,"stringValidatorMessageGenerator",this.generateString("description")));
//        columnDefs.add(getColumnDef("i18n", "ProjectConfig", "国际化", ColumnType.NORMAL, HeaderType.NORMAL, CellType.NORMAL, ControlType.CHECK_BOX, DataSourceType.NULL, true, true,null,null));
//
//        columnDefs.add(getColumnDef("modulePrefix", "ProjectConfig", "模块前缀", ColumnType.NORMAL, HeaderType.NORMAL_FILTER, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, true, true,null,null));
//        columnDefs.add(getColumnDef("scanPackage", "ProjectConfig", "扫描的包路径", ColumnType.NORMAL, HeaderType.NORMAL_FILTER, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, true, true,null,null));
//        columnDefs.add(getColumnDef("architectures", "ProjectConfig", "代码架构", ColumnType.NORMAL, HeaderType.NORMAL_FILTER, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, true, true,null,null));
//        columnDefs.add(getColumnDef("config", "ProjectConfig", "脚手架配置", ColumnType.NORMAL, HeaderType.NORMAL_FILTER, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, true, true,null,null));
//        columnDefs.add(getColumnDef("wrapWithParent", "ProjectConfig", "是否使用父module", ColumnType.NORMAL, HeaderType.NORMAL, CellType.NORMAL, ControlType.CHECK_BOX, DataSourceType.NULL, false, true,null,null));
//        columnDefs.add(getColumnDef("scaffold", "ProjectConfig", "脚手架", ColumnType.NORMAL, HeaderType.NORMAL, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, false, true,null,null));
//        columnDefs.add(getColumnDef("Actions", "ProjectConfig", "操作", ColumnType.ACTION, HeaderType.NORMAL, CellType.OPERATION, ControlType.INPUT_TEXT, DataSourceType.NULL, false, true,null,null));
//        columnDefs.add(getColumnDef("Filters", "ProjectConfig", "过滤", ColumnType.FILTER, HeaderType.COLUMN_FILTER, CellType.NORMAL, ControlType.INPUT_TEXT, DataSourceType.NULL, false, true,null,null));
    }

    private ColumnDef getColumnDef(String propertyName,
                                   String tableClassName,
                                   String chineseName,
                                   ColumnType columnType,
                                   HeaderType headerType,
                                   CellType cellType,
                                   ControlType controlType,
                                   DatasourceType dataSourceType,
                                   Boolean enableHiding,
                                   Boolean showInList,
                                   String validateType,
                                   Validator validator
    ) {
        ColumnDef columnDef = new ColumnDef();
        columnDef.setPropertyName(propertyName);
        columnDef.setChineseName(chineseName);
        columnDef.setCellType(cellType.getIdentity());
        columnDef.setColumnType(columnType.getIdentity());
        columnDef.setHeaderType(headerType.getIdentity());
        columnDef.setControlType(controlType.getIdentity());
        columnDef.setDatasourceType(dataSourceType.getIdentity());
        columnDef.setEnableHidden(enableHiding);
        columnDef.setSubsidiaryColumns("");
        columnDef.setShowInList(showInList);
        columnDef.setShowInEdit(true);
        columnDef.setValidateType(validateType);
        if(StringUtility.isNullOrEmpty(validateType)){
            columnDef.setValidateType("nullableValidatorMessageGenerator");
        }
        columnDef.setValidator(validator);
        return columnDef;
    }

    @Override
    public TableConfigBO getTableConfig(Long tableConfigId) {
        TableConfigBO tableConfig = new TableConfigBO();
        tableConfig.setId(tableConfigId);
        tableConfig.setProjectId(1L);
        tableConfig.setPrimaryKey("id");
        tableConfig.setTableName("t_table_config");
        tableConfig.setClassName("com.sparrowzoo.coder.po.TableConfig");
        tableConfig.setDescription("");
        tableConfig.setCheckable(1);
        tableConfig.setRowMenu(99);
        tableConfig.setColumnFilter(100);
        tableConfig.setStatusCommand(false);
        tableConfig.setColumnConfigs("");
        tableConfig.setSource(CodeSource.INNER.getIdentity());
        tableConfig.setSourceCode("");
        tableConfig.setCreateUserId(0L);
        tableConfig.setModifiedUserId(0L);
        tableConfig.setGmtCreate(0L);
        tableConfig.setGmtModified(0L);
        tableConfig.setCreateUserName("harry");
        tableConfig.setModifiedUserName("");

//        TableContext tableContext=new TableContext(tableConfig,null);
//        List<ColumnDef> columnDefs =tableContext.getDefaultColumns();
//        tableConfig.setColumnConfigs(JSON.toJSONString(columnDefs));
        return tableConfig;
    }

    @Override
    public List<TableConfigBO> queryTableConfigs(TableConfigQuery tableConfigQuery) {
        TableConfigBO tableConfig = new TableConfigBO();
        tableConfig.setId(1L);
        tableConfig.setProjectId(1L);
        tableConfig.setLocked(false);
        tableConfig.setPrimaryKey("id");
        tableConfig.setTableName("t_table_config");
        tableConfig.setClassName("com.sparrowzoo.coder.po.TableConfig");
        tableConfig.setDescription("");
        tableConfig.setCheckable(1);
        tableConfig.setRowMenu(99);
        tableConfig.setColumnFilter(100);
        tableConfig.setStatusCommand(false);

        tableConfig.setSource(CodeSource.INNER.getIdentity());
        tableConfig.setSourceCode("");
        tableConfig.setCreateUserId(0L);
        tableConfig.setModifiedUserId(0L);
        tableConfig.setGmtCreate(0L);
        tableConfig.setGmtModified(0L);
        tableConfig.setCreateUserName("harry");
        tableConfig.setModifiedUserName("");



        TableConfigBO projectTable = new TableConfigBO();
        projectTable.setId(2L);
        projectTable.setProjectId(1L);
        projectTable.setPrimaryKey("id");
        projectTable.setTableName("t_project_config");
        projectTable.setClassName("com.sparrowzoo.coder.po.ProjectConfig");
        projectTable.setLocked(false);

        projectTable.setDescription("");
        projectTable.setCheckable(1);
        projectTable.setRowMenu(99);
        projectTable.setColumnFilter(100);
        projectTable.setStatusCommand(false);
        projectTable.setColumnConfigs("");
        projectTable.setSource(CodeSource.INNER.getIdentity());
        projectTable.setSourceCode("");
        projectTable.setCreateUserId(0L);
        projectTable.setModifiedUserId(0L);
        projectTable.setGmtCreate(0L);
        projectTable.setGmtModified(0L);
        projectTable.setCreateUserName("harry");
        projectTable.setModifiedUserName("");


        TableConfigBO columnConfig = new TableConfigBO();
        columnConfig.setId(2L);
        columnConfig.setProjectId(1L);
        columnConfig.setPrimaryKey("id");
        columnConfig.setTableName("t_column_config");
        columnConfig.setClassName("com.sparrowzoo.coder.domain.bo.ColumnDef");
        columnConfig.setLocked(false);

        columnConfig.setDescription("");
        columnConfig.setCheckable(-1);
        columnConfig.setRowMenu(-1);
        columnConfig.setColumnFilter(-1);
        columnConfig.setStatusCommand(false);
        columnConfig.setColumnConfigs("");
        columnConfig.setSource(CodeSource.INNER.getIdentity());
        columnConfig.setSourceCode("");
        columnConfig.setCreateUserId(0L);
        columnConfig.setModifiedUserId(0L);
        columnConfig.setGmtCreate(0L);
        columnConfig.setGmtModified(0L);
        columnConfig.setCreateUserName("harry");
        columnConfig.setModifiedUserName("");


        List<TableConfigBO> list = new ArrayList<>();
        list.add(projectTable);
        list.add(tableConfig);
        list.add(columnConfig);
        return list;
    }

    @Override
    public Long getTableConfigCount(TableConfigQuery tableConfigQuery) {
        return null;
    }

    public RegexValidator generateRegex(String propertyName) {
        RegexValidator validator = new RegexValidator();
        validator.setAllowEmpty(true);
        validator.setI18n(false);
        validator.setMinLength(5);
        validator.setMaxLength(30);
        validator.setPropertyName(propertyName);
        return validator;
    }

    public StringValidator generateString(String propertyName) {
        RegexValidator validator = new RegexValidator();

        validator.setAllowEmpty(false);
        validator.setI18n(true);
        validator.setMinLength(5);
        validator.setMaxLength(30);
        validator.setPropertyName(propertyName);
        return validator;
    }

    public DigitalValidator generateDigital(String propertyName) {
        DigitalValidator validator = new DigitalValidator();
        validator.setAllowEmpty(true);
        validator.setI18n(false);
        validator.setMinValue(5);
        validator.setMaxValue(30);
        validator.setCategory(DigitalCategory.INTEGER);
        validator.setPropertyName(propertyName);
        return validator;
    }
}
