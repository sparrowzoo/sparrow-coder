package com.sparrowzoo.coder.service.registry;

import com.sparrowzoo.coder.po.ProjectConfig;
import com.sparrowzoo.coder.po.TableConfig;
import com.sparrowzoo.coder.bo.TableContext;
import com.sparrowzoo.coder.service.EnvConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考虑上线后多用户访问
 * 为防止内存溢出，这里使用临时变量，每次用户请求量初始化，使用后释放
 */
@Slf4j
@Data
public class TableConfigRegistry {
    public TableConfigRegistry() {
        log.info("init table config ");
    }

    private Map<String, TableContext> registry = new HashMap<>();
    private ProjectConfig project;
    private EnvConfig envConfig;

    public EnvConfig getEnvConfig() {
        return envConfig;
    }

    public void register(String tableName, TableContext tableContext) {
        registry.put(tableName, tableContext);
    }

    public TableContext getTableContext(String tableName) {
        return registry.get(tableName);
    }

    public TableContext getFirstTableContext() {
        return registry.values().iterator().next();
    }

    public List<TableConfig> getAllTableConfig() {
        List<TableConfig> tableConfigList = new ArrayList<>();
        for (String tableName : this.registry.keySet()) {
            tableConfigList.add(this.registry.get(tableName).getTableConfig());
        }
        return tableConfigList;
    }

    public void register(ProjectConfig projectConfig) {
        this.project = projectConfig;
    }

    public ProjectConfig getProjectConfig() {
        return this.project;
    }
}
