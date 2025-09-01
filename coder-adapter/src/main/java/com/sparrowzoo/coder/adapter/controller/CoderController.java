package com.sparrowzoo.coder.adapter.controller;

import com.alibaba.fastjson.JSON;
import com.sparrow.cg.impl.DynamicCompiler;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.BusinessException;
import com.sparrowzoo.coder.constant.DefaultSpecialColumnIndex;
import com.sparrowzoo.coder.domain.CoderDomainRegistry;
import com.sparrowzoo.coder.domain.service.CodeGenerator;
import com.sparrowzoo.coder.domain.service.DefaultCodeGenerator;
import com.sparrowzoo.coder.domain.service.EnvConfig;
import com.sparrowzoo.coder.domain.service.TableConfigService;
import com.sparrowzoo.coder.enums.CodeSource;
import com.sparrowzoo.coder.protocol.enums.CoderError;
import com.sparrowzoo.coder.protocol.param.LocalClassParam;
import com.sparrowzoo.coder.protocol.param.SourceCodeParam;
import com.sparrowzoo.coder.protocol.param.TableConfigParam;
import com.sparrowzoo.coder.protocol.query.ProjectTablesQuery;
import com.sparrowzoo.coder.utils.DefaultColumnsDefCreator;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;

@RestController
@RequestMapping("coder")
@Api(value = "CoderConfig", tags = "CoderConfig")
public class CoderController {
    @Inject
    private TableConfigService tableConfigService;

    @Inject
    private EnvConfig envConfig;

    @Inject
    private CoderDomainRegistry domainRegistry;

    @PostMapping("init-by-local.json")
    public void localInit(@RequestBody LocalClassParam className) throws BusinessException {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className.getFullClassName());
        } catch (ClassNotFoundException e) {
            throw new BusinessException(CoderError.CLASS_NOT_FOUND);
        }
        SparrowEntityManager entityManager = new SparrowEntityManager(clazz);
        TableConfigParam tableConfigParam = new TableConfigParam();
        tableConfigParam.setProjectId(className.getProjectId());
        tableConfigParam.setPrimaryKey(entityManager.getPrimary().getPropertyName());
        tableConfigParam.setTableName(entityManager.getTableName());
        tableConfigParam.setClassName(className.getFullClassName());
        tableConfigParam.setDescription("");
        tableConfigParam.setLocked(false);
        tableConfigParam.setCheckable(DefaultSpecialColumnIndex.CHECK);
        tableConfigParam.setRowMenu(DefaultSpecialColumnIndex.ROW_MENU);
        tableConfigParam.setColumnFilter(DefaultSpecialColumnIndex.COLUMN_FILTER);
        tableConfigParam.setStatusCommand(true);
        tableConfigParam.setColumnConfigs(JSON.toJSONString(DefaultColumnsDefCreator.create(tableConfigParam.getClassName())));
        tableConfigParam.setSource(CodeSource.INNER.getIdentity());
        tableConfigParam.setSourceCode("");
        tableConfigParam.setOnlyAccessSelf(true);
        tableConfigParam.setPageSize(10);
        this.tableConfigService.saveTableConfig(tableConfigParam);
    }

    @PostMapping("init-by-jpa.json")
    public void jpaInit(@RequestBody SourceCodeParam sourceCodeParam) throws BusinessException {
        Class<?> clazz = DynamicCompiler.getInstance().source2Class(sourceCodeParam.getFullClassName(), sourceCodeParam.getSourceCode());
        SparrowEntityManager entityManager = new SparrowEntityManager(clazz);
        TableConfigParam tableConfigParam = new TableConfigParam();
        tableConfigParam.setProjectId(sourceCodeParam.getProjectId());
        tableConfigParam.setPrimaryKey(entityManager.getPrimary().getPropertyName());
        tableConfigParam.setTableName(entityManager.getTableName());
        tableConfigParam.setClassName(sourceCodeParam.getFullClassName());
        tableConfigParam.setDescription("");
        tableConfigParam.setLocked(false);
        tableConfigParam.setCheckable(DefaultSpecialColumnIndex.CHECK);
        tableConfigParam.setRowMenu(DefaultSpecialColumnIndex.ROW_MENU);
        tableConfigParam.setColumnFilter(DefaultSpecialColumnIndex.COLUMN_FILTER);
        tableConfigParam.setStatusCommand(true);
        tableConfigParam.setColumnConfigs(JSON.toJSONString(DefaultColumnsDefCreator.create(tableConfigParam.getClassName())));
        tableConfigParam.setSource(CodeSource.SOURCE_CODE.getIdentity());
        tableConfigParam.setSourceCode(sourceCodeParam.getSourceCode());
        tableConfigParam.setPageSize(10);
        tableConfigParam.setOnlyAccessSelf(true);


        this.tableConfigService.saveTableConfig(tableConfigParam);
    }


    @PostMapping("clear.json")
    public void clear(@RequestBody Long projectId) throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(projectId, envConfig, domainRegistry);
        generator.clear();
    }

    @PostMapping("init-scaffold.json")
    public void initScaffold(@RequestBody Long projectId) throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(projectId, envConfig, domainRegistry);
        generator.initScaffold();
    }

    @PostMapping("generate.json")
    public void generate(@RequestBody ProjectTablesQuery projectTablesQuery) throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(projectTablesQuery.getProjectId(), envConfig, domainRegistry);
        for (String tableName : projectTablesQuery.getTableNames()) {
            generator.generate(tableName);
        }
    }
}
