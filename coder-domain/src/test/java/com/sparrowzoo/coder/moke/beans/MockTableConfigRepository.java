package com.sparrowzoo.coder.moke.beans;

import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.enums.CodeSource;
import com.sparrowzoo.coder.po.TableConfig;
import com.sparrowzoo.coder.protocol.param.TableConfigParam;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;
import com.sparrowzoo.coder.repository.TableConfigRepository;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
public class MockTableConfigRepository implements TableConfigRepository {
    @Override
    public Long save(TableConfigParam tableConfigParam) {
        return null;
    }

    @Override
    public Integer delete(String tableConfigIds) {
        return null;
    }

    @Override
    public Integer disable(String tableConfigIds) {
        return null;
    }

    @Override
    public Integer enable(String tableConfigIds) {
        return null;
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
        tableConfig.setCheckable(false);
        tableConfig.setRowMenu(false);
        tableConfig.setColumnFilter(false);
        tableConfig.setStatusCommand(false);
        tableConfig.setColumnConfigs("");
        tableConfig.setSource(CodeSource.INNER.name());
        tableConfig.setSourceCode("");
        tableConfig.setCreateUserId(0L);
        tableConfig.setModifiedUserId(0L);
        tableConfig.setGmtCreate(0L);
        tableConfig.setGmtModified(0L);
        tableConfig.setCreateUserName("harry");
        tableConfig.setModifiedUserName("");
        return tableConfig;
    }

    @Override
    public List<TableConfigBO> queryTableConfigs(TableConfigQuery tableConfigQuery) {

        TableConfigBO tableConfig = new TableConfigBO();
        tableConfig.setId(1L);
        tableConfig.setProjectId(1L);
        tableConfig.setPrimaryKey("id");
        tableConfig.setTableName("t_table_config");
        tableConfig.setClassName("com.sparrowzoo.coder.po.TableConfig");
        tableConfig.setDescription("");
        tableConfig.setCheckable(false);
        tableConfig.setRowMenu(false);
        tableConfig.setColumnFilter(false);
        tableConfig.setStatusCommand(false);
        tableConfig.setColumnConfigs("");
        tableConfig.setSource(CodeSource.INNER.name());
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
        projectTable.setDescription("");
        projectTable.setCheckable(false);
        projectTable.setRowMenu(false);
        projectTable.setColumnFilter(false);
        projectTable.setStatusCommand(false);
        projectTable.setColumnConfigs("");
        projectTable.setSource(CodeSource.INNER.name());
        projectTable.setSourceCode("");
        projectTable.setCreateUserId(0L);
        projectTable.setModifiedUserId(0L);
        projectTable.setGmtCreate(0L);
        projectTable.setGmtModified(0L);
        projectTable.setCreateUserName("harry");
        projectTable.setModifiedUserName("");
       List<TableConfigBO> list=new ArrayList<>();
       list.add(tableConfig);
       list.add(projectTable);
       return list;
    }

    @Override
    public Long getTableConfigCount(TableConfigQuery tableConfigQuery) {
        return null;
    }
}
