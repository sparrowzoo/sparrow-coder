package com.sparrowzoo.coder.service.registry;

import com.sparrowzoo.coder.po.ProjectConfig;
import com.sparrowzoo.coder.po.TableConfig;
import com.sparrowzoo.coder.bo.TableContext;
import com.sparrowzoo.coder.service.EnvConfig;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每次源代码时，只初始化一次
 */
@Named
@Slf4j
public class TableConfigRegistry {
    public TableConfigRegistry() {
        log.info("init table config ");
    }

    private Map<Long,Map<String, TableContext>> registry = new HashMap<>();
    private Map<Long,ProjectConfig> projects = new HashMap<>();
    @Inject
    private EnvConfig envConfig;

    public EnvConfig getEnvConfig() {
        return envConfig;
    }

    public void register(Long projectId, String tableName, TableContext tableContext) {
        registry.putIfAbsent(projectId, new HashMap<>());
        registry.get(projectId).put(tableName, tableContext);
    }

    public TableContext getFirstTableContext(Long projectId, String tableName) {
        return registry.get(projectId).get(tableName);
    }

    public TableContext getFirstTableContext(Long projectId) {
        return registry.get(projectId).values().iterator().next();
    }

    public List<TableConfig> getAllTableConfig(Long projectId) {
        List<TableConfig> tableConfigList = new ArrayList<>();
        for (String tableName : this.registry.get(projectId).keySet()) {
            tableConfigList.add(this.registry.get(projectId).get(tableName).getTableConfig());
        }
        return tableConfigList;
    }

    public void register(ProjectConfig projectConfig) {
        this.projects.put(projectConfig.getId(), projectConfig);
    }

    public ProjectConfig getProjectConfig(Long projectId) {
        return this.projects.get(projectId);
    }
}
