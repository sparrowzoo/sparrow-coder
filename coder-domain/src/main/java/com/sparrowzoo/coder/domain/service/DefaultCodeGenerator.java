package com.sparrowzoo.coder.domain.service;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.FileUtility;
import com.sparrowzoo.coder.domain.DomainRegistry;
import com.sparrowzoo.coder.domain.bo.*;
import com.sparrowzoo.coder.domain.service.backend.ScaffoldCopier;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;
import com.sparrowzoo.coder.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


@Slf4j
public class DefaultCodeGenerator implements CodeGenerator {
    private TableConfigRegistry registry;
    private DomainRegistry domainRegistry;

    public DefaultCodeGenerator(Long projectId, EnvConfig envConfig, DomainRegistry domainRegistry) throws IOException, ClassNotFoundException {
        this.domainRegistry = domainRegistry;
        ProjectConfigBO projectConfig = domainRegistry.getProjectConfigRepository().getProjectConfig(projectId);
        Properties config = ConfigUtils.initPropertyConfig(projectConfig.getConfig());
        ProjectBO project = new ProjectBO(projectConfig, config, envConfig);
        this.registry = new TableConfigRegistry(project);
        this.initRegistry();
    }


    public void initRegistry() {
        TableConfigQuery tableConfigQuery = new TableConfigQuery();
        tableConfigQuery.setProjectId(registry.getProject().getProjectConfig().getId());
        tableConfigQuery.setDeleted(false);
        tableConfigQuery.setStatus(StatusRecord.ENABLE.ordinal());
        List<TableConfigBO> tableConfigs = domainRegistry.getTableConfigRepository().queryTableConfigs(tableConfigQuery);
        for (TableConfigBO tableConfigBO : tableConfigs) {
            TableContext context = new TableContext(tableConfigBO, this.registry.getProject());
            this.registry.register(tableConfigBO.getTableName(), context);
        }
    }


    @Override
    public void generate(String tableName) throws IOException {
        TableContext context = registry.getTableContext(tableName);
        if (context == null) {
            throw new IllegalArgumentException("table " + tableName + " not found");
        }
        if (context.getTableConfig().getLocked()) {
            log.info("table {} is locked, skip generate", context.getTableConfig().getTableName());
            //return;
        }
        ProjectArchsBO architectures = registry.getProject().getArchitectures();
        for (String architectureCategory : architectures.getArchs().keySet()) {
            ArchitectureGenerator architectureGenerator = architectures.getArch(architectureCategory);
            if (architectureGenerator != null) {
                architectureGenerator.generate(registry, tableName);
            }
        }
    }

    @Override
    public void initScaffold() {
        ScaffoldCopier.copy(registry);
    }

    @Override
    public void clear() {
        ProjectBO project = registry.getProject();
        String targetDirectoryPath =
                new FileNameBuilder(project.getEnvConfig().getWorkspace())
                        .joint(String.valueOf(project.getProjectConfig().getCreateUserId()))
                        .joint(project.getProjectConfig().getName())
                        .build();
        FileUtility.getInstance().delete(targetDirectoryPath, System.currentTimeMillis());
    }

    @Override
    public TableConfigRegistry getRegistry() {
        return this.registry;
    }


}
