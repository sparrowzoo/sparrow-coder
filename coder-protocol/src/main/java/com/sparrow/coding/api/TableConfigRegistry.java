package com.sparrow.coding.api;

import com.sparrow.coding.po.ProjectConfig;
import com.sparrow.coding.po.TableConfig;
import com.sparrow.coding.protocol.TableContext;
import com.sparrow.container.ConfigReader;
import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每次源代码时，只初始化一次
 */
@Named
public class TableConfigRegistry {
    private Map<String, TableContext> registry = new HashMap<>();
    private ProjectConfig project;

    private String workspace;

    private String scaffold;

    public void register(String tableName, TableContext tableContext) {
        registry.put(tableName, tableContext);
    }

    public TableContext getTableContext(String tableName) {
        return registry.get(tableName);
    }

    public List<TableConfig> getAllTableConfig(Long projectId) {
        List<TableConfig> tableConfigList = new ArrayList<>();
        for (String tableName : this.registry.keySet()) {
            tableConfigList.add(this.registry.get(tableName).getTableConfig());
        }
        return tableConfigList;
    }

    public void register(ProjectConfig projectConfig) {
        this.project = projectConfig;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getScaffold() {
        return scaffold;
    }

    public void setScaffold(String scaffold) {
        this.scaffold = scaffold;
    }

    public ProjectConfig getProjectConfig() {
        return this.project;
    }
}
