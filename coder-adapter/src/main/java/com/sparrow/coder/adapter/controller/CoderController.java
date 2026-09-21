package com.sparrow.coder.adapter.controller;

import com.alibaba.fastjson.JSON;
import com.sparrow.cg.impl.DynamicCompiler;
import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.context.SessionContext;
import com.sparrow.exception.Asserts;
import com.sparrow.io.FolderFilter;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.LoginUser;
import com.sparrow.coder.constant.DefaultSpecialColumnIndex;
import com.sparrow.coder.domain.CoderDomainRegistry;
import com.sparrow.coder.domain.service.CodeGenerator;
import com.sparrow.coder.domain.service.DefaultCodeGenerator;
import com.sparrow.coder.domain.service.EnvConfig;
import com.sparrow.coder.domain.service.TableConfigService;
import com.sparrow.coder.enums.CodeSource;
import com.sparrow.coder.protocol.enums.CoderError;
import com.sparrow.coder.protocol.param.LocalClassParam;
import com.sparrow.coder.protocol.param.SourceCodeParam;
import com.sparrow.coder.protocol.param.TableConfigParam;
import com.sparrow.coder.protocol.query.ProjectTablesQuery;
import com.sparrow.coder.utils.DefaultColumnsDefCreator;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.protocol.dao.PO;
import com.sparrow.utility.CompressUtility;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("coder")
@Tag(name = "CoderConfig", description = "CoderConfig")
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

        Asserts.isTrue(!className.getFullClassName().contains(".po."), CoderError.CLASS_NOT_CONTAINS_PO);
        Asserts.isTrue(isSystemTable(className), CoderError.SYSTEM_TABLE);
        Asserts.isTrue(!PO.class.isAssignableFrom(clazz), CoderError.CLASS_CAN_NOT_ASSIGNABLE_PO);
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


    @PostMapping("clear-scaffold.json")
    public void clear(@RequestBody Long projectId) throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(projectId, envConfig, domainRegistry);
        generator.clear();
    }

    @PostMapping("init-scaffold.json")
    public void initScaffold(@RequestBody Long projectId) throws IOException, ClassNotFoundException {
        CodeGenerator generator = new DefaultCodeGenerator(projectId, envConfig, domainRegistry);
        generator.initScaffold();
    }

    private boolean isSystemTable(LocalClassParam localClassParam) {
        if (localClassParam.getFullClassName().equals("com.sparrow.coder.po.ProjectConfig") || localClassParam.getFullClassName().equals("com.sparrow.coder.po.TableConfig")) {
            return true;
        }
        return false;
    }

    @PostMapping("generate.json")
    public void generate(@RequestBody ProjectTablesQuery projectTablesQuery) throws IOException, BusinessException {
        CodeGenerator generator = new DefaultCodeGenerator(projectTablesQuery.getProjectId(), envConfig, domainRegistry);
        for (String tableName : projectTablesQuery.getTableNames()) {
            generator.generate(tableName);
        }
    }


    @PostMapping("zip-download.json")
    public void zipDownload(@RequestBody ProjectTablesQuery projectTablesQuery, HttpServletResponse response) throws IOException, BusinessException {
        CodeGenerator generator = new DefaultCodeGenerator(projectTablesQuery.getProjectId(), envConfig, domainRegistry);
        TableConfigRegistry registry = generator.getRegistry();
        ProjectConfigBO projectConfig = registry.getProject().getProjectConfig();
        LoginUser loginUser = SessionContext.getLoginUser();
        Asserts.isTrue(!projectConfig.getCreateUserId().equals(loginUser.getUserId()), SparrowError.SYSTEM_PERMISSION_DENIED);
        EnvConfig envConfig = registry.getProject().getEnvConfig();
        String home = envConfig.getHome(projectConfig.getCreateUserId());
        String projectName = projectConfig.getName();
        String targetPath = new FileNameBuilder(envConfig.getWorkspace()).joint(envConfig.getProjectRoot()).joint(home).joint(projectName).build();

        FolderFilter folderFilter = new FolderFilter() {
            @Override
            public Boolean filter(String sourceFile) {
                if (sourceFile.endsWith("node_modules") || sourceFile.endsWith("out") || sourceFile.endsWith(".idea") || sourceFile.endsWith(".next") || sourceFile.endsWith("target")) {
                    return true;
                }
                return false;
            }
        };

        response.resetBuffer();
        try {
            response.setContentType("application/zip");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            String fileName = projectName + ".zip";
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\";filename*=UTF-8''" + encodedFileName);
            CompressUtility.zipDir(targetPath, response.getOutputStream(), folderFilter);
        } catch (Exception e) {
            response.setContentType("application/json");
            response.setHeader("Content-Disposition", "");
            throw new BusinessException(SparrowError.SYSTEM_SERVER_ERROR);
        }
    }
}
