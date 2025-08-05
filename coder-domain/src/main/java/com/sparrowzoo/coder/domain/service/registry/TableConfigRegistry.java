package com.sparrowzoo.coder.domain.service.registry;

import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
    public TableConfigRegistry(ProjectBO project) {
        this.project=project;
        this.registry=new HashMap<>();
    }



    private Map<String, TableContext> registry;
    //封装project 维度变量，避免table context 引用registry循环依赖
    private ProjectBO project;

    private PlaceholderExtensionRegistry placeholderExtensionRegistry=PlaceholderExtensionRegistry.getInstance();

    public void register(TableContext tableContext) {
        registry.put(tableContext.getTableConfig().getTableName(), tableContext);
        this.placeholderExtensionRegistry.extension(tableContext,this);
    }

    public void dependency(TableContext tableContext){
        this.placeholderExtensionRegistry.dependencyExtension(tableContext,this);
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
}
