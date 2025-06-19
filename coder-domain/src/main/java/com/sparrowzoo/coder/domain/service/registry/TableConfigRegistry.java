package com.sparrowzoo.coder.domain.service.registry;

import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.EnvConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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
    private ProjectConfigBO project;
    private Properties projectConfig;
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

    public List<TableConfigBO> getAllTableConfig() {
        List<TableConfigBO> tableConfigList = new ArrayList<>();
        for (String tableName : this.registry.keySet()) {
            tableConfigList.add(this.registry.get(tableName).getTableConfig());
        }
        return tableConfigList;
    }

    public void register(ProjectConfigBO projectConfig) {
        this.project = projectConfig;
    }
}
