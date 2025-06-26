package com.sparrowzoo.coder.domain.service;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.FileUtility;
import com.sparrowzoo.coder.domain.DomainRegistry;
import com.sparrowzoo.coder.domain.bo.ProjectArchsBO;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.backend.ClassPlaceholder;
import com.sparrowzoo.coder.domain.service.backend.DefaultClassPlaceholder;
import com.sparrowzoo.coder.domain.service.backend.ScaffoldCopier;
import com.sparrowzoo.coder.domain.service.frontend.DefaultFrontendPlaceholder;
import com.sparrowzoo.coder.domain.service.frontend.FrontendPlaceholder;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;
import com.sparrowzoo.coder.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;


@Slf4j
public class DefaultCodeGenerator implements CodeGenerator {
    private TableConfigRegistry registry;
    private DomainRegistry domainRegistry;
    private ProjectArchsBO projectArchs;

    public DefaultCodeGenerator(Long projectId, EnvConfig envConfig, DomainRegistry domainRegistry) throws IOException, ClassNotFoundException {
        this.registry = new TableConfigRegistry();
        ProjectConfigBO projectConfig = domainRegistry.getProjectConfigRepository().getProjectConfig(projectId);
        this.registry.setProject(projectConfig);
        this.registry.setEnvConfig(envConfig);
        this.domainRegistry = domainRegistry;
        this.projectArchs = new ProjectArchsBO(registry.getProject().getArchitectures());

        Properties config = ConfigUtils.initPropertyConfig(registry.getProject().getConfig());
        registry.setProjectConfig(config);
        this.initRegistry(registry);
    }


    @Override
    public void initRegistry(TableConfigRegistry registry) throws ClassNotFoundException, IOException {
        TableConfigQuery tableConfigQuery = new TableConfigQuery();
        tableConfigQuery.setProjectId(registry.getProject().getId());
        tableConfigQuery.setDeleted(false);
        tableConfigQuery.setStatus(StatusRecord.ENABLE.ordinal());
        List<TableConfigBO> tableConfigs = domainRegistry.getTableConfigRepository().queryTableConfigs(tableConfigQuery);
        for (TableConfigBO tableConfigBO : tableConfigs) {
            TableContext context = new TableContext(tableConfigBO);
            this.registry.register(tableConfigBO.getTableName(), context);
            this.initPlaceHolder(context);
        }
    }


    private Map<String, String> initPlaceHolder(TableContext tableContext) throws ClassNotFoundException {

        Map<String, String> placeHolder = new TreeMap<>(Comparator.reverseOrder());
        tableContext.setPlaceHolder(placeHolder);
        TableConfigBO tableConfig = tableContext.getTableConfig();
        ClassPlaceholder classPlaceHolder = new DefaultClassPlaceholder(this.registry, tableConfig.getTableName());
        classPlaceHolder.init();
        ArchitectureGenerator frontGenerator = this.projectArchs.getArch(ArchitectureCategory.FRONTEND);
        FrontendPlaceholder frontendArchAccessor = new DefaultFrontendPlaceholder(this.registry, tableConfig.getTableName(),frontGenerator.getName());
        frontendArchAccessor.init();
        return placeHolder;
    }

    @Override
    public void generate(String tableName) throws IOException {
        TableContext context = registry.getTableContext(tableName);
        if (context.getTableConfig().getLocked()) {
            log.info("table {} is locked, skip generate", context.getTableConfig().getTableName());
            //return;
        }
        ProjectArchsBO architectures = new ProjectArchsBO(registry.getProject().getArchitectures());
        for (String architectureCategory : architectures.getArchs().keySet()) {
            architectures.getArch(architectureCategory).generate(registry, tableName);
        }

//        ArchitectureRegistry.getInstance().getRegistry()
//                .get(ArchitectureCategory.BACKEND)
//                .get("clearArchitectureGenerator").generate(registry, tableName);
//
//        ArchitectureRegistry.getInstance().getRegistry()
//                .get(ArchitectureCategory.DATABASE)
//                .get("mySqlArchitectureGenerator").generate(registry, tableName);

    }

    @Override
    public void initScaffold() {
        ScaffoldCopier.copy(registry);
    }

    @Override
    public void clear() {
        String targetDirectoryPath =
                new FileNameBuilder(registry.getEnvConfig().getWorkspace())
                        .joint(String.valueOf(registry.getProject().getCreateUserId()))
                        .joint(registry.getProject().getName())
                        .build();
        FileUtility.getInstance().delete(targetDirectoryPath, System.currentTimeMillis());
    }
}
