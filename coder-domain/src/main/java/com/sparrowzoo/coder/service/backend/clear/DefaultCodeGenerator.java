package com.sparrowzoo.coder.service.backend.clear;

import com.sparrow.coding.api.backend.ClassGenerator;
import com.sparrow.coding.enums.ClassKey;
import com.sparrow.coding.enums.CodeSource;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.utility.DateTimeUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.po.ProjectConfig;
import com.sparrowzoo.coder.po.TableConfig;
import com.sparrowzoo.coder.bo.TableContext;
import com.sparrowzoo.coder.enums.PlaceholderKey;
import com.sparrowzoo.coder.service.CodeGenerator;
import com.sparrowzoo.coder.service.backend.ScaffoldCopier;
import com.sparrowzoo.coder.service.registry.TableConfigRegistry;

import java.io.IOException;
import java.util.*;


public class DefaultCodeGenerator implements CodeGenerator {

    public DefaultCodeGenerator(Long projectId) throws IOException, ClassNotFoundException {
        registry = ApplicationContext.getContainer().getBean(TableConfigRegistry.class);
        this.initRegistry(projectId);
    }

    private TableConfigRegistry registry;

    @Override
    public void initRegistry(Long projectId) throws ClassNotFoundException, IOException {
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setId(1L);
        projectConfig.setName("sparrow-coder");
        projectConfig.setDescription("授权");
        projectConfig.setArchitectures("template");
        projectConfig.setFrontendName("react-sparrow-admin");
        projectConfig.setChineseName("授权管理");
        projectConfig.setDescription("");
        projectConfig.setModulePrefix("coder");
        projectConfig.setScanPackage("com.sparrow");
        projectConfig.setConfig("");
        projectConfig.setCreateUserId(1L);
        projectConfig.setModifiedUserId(0L);
        projectConfig.setGmtCreate(1L);
        projectConfig.setGmtModified(0L);
        projectConfig.setCreateUserName("harry");
        projectConfig.setModifiedUserName("");
        projectConfig.setWrapWithParent(false);

        this.registry.register(projectConfig);
        TableConfig tableConfig = new TableConfig();
        tableConfig.setId(0L);
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

        TableContext context = new TableContext();
        context.setTableConfig(tableConfig);
        this.registry.register(projectId,tableConfig.getTableName(), context);
        this.initPlaceHolder(context);
    }


    private Map<String, String> initPlaceHolder(TableContext tableContext) throws IOException, ClassNotFoundException {
        Map<String, String> placeHolder = new TreeMap<>(Comparator.reverseOrder());
        tableContext.setPlaceHolder(placeHolder);
        ProjectConfig projectConfig = this.registry.getProjectConfig(tableContext.getTableConfig().getProjectId());
        TableConfig tableConfig = tableContext.getTableConfig();
        EntityManager entityManager = new SparrowEntityManager(Class.forName(tableConfig.getClassName()));
        String tableName = entityManager.getTableName();
        String persistenceClassName = entityManager.getSimpleClassName();
        String modulePrefix = projectConfig.getModulePrefix();
        ClassGenerator classGenerator = new DefaultClassGenerator(this.registry,projectConfig.getId());
        placeHolder.put(PlaceholderKey.$module_prefix.name(), modulePrefix);
        placeHolder.put(PlaceholderKey.$origin_table_name.name(), tableName);
        placeHolder.put(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
        placeHolder.put(PlaceholderKey.$persistence_object_name.name(), StringUtility.setFirstByteLowerCase(persistenceClassName));
        placeHolder.put(PlaceholderKey.$persistence_object_by_horizontal.name(), StringUtility.humpToLower(persistenceClassName, '-'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_slash.name(), StringUtility.humpToLower(persistenceClassName, '/'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_dot.name(), StringUtility.humpToLower(persistenceClassName, '.'));

        placeHolder.put(PlaceholderKey.$date.name(), DateTimeUtility.getFormatCurrentTime("yyyy-MM-dd HH:mm:ss"));

        placeHolder.put(PlaceholderKey.$package_po.name(), tableContext.getPoPackage());
        placeHolder.put(PlaceholderKey.$package_bo.name(), classGenerator.getPackage( ClassKey.BO));
        placeHolder.put(PlaceholderKey.$package_param.name(), classGenerator.getPackage( ClassKey.PARAM));

        placeHolder.put(PlaceholderKey.$package_query.name(), classGenerator.getPackage( ClassKey.QUERY));
        placeHolder.put(PlaceholderKey.$package_dto.name(), classGenerator.getPackage( ClassKey.DTO));
        placeHolder.put(PlaceholderKey.$package_vo.name(), classGenerator.getPackage( ClassKey.VO));

        placeHolder.put(PlaceholderKey.$package_dao.name(), classGenerator.getPackage( ClassKey.DAO));
        String pagerQuery = classGenerator.getPackage(ClassKey.PAGER_QUERY);
        placeHolder.put(PlaceholderKey.$package_pager_query.name(), StringUtility.replace(pagerQuery, placeHolder));
        placeHolder.put(PlaceholderKey.$package_repository.name(), classGenerator.getPackage( ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$package_repository_impl.name(), classGenerator.getPackage( ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$package_data_converter.name(), classGenerator.getPackage( ClassKey.DATA_CONVERTER));
        placeHolder.put(PlaceholderKey.$package_assemble.name(), classGenerator.getPackage( ClassKey.ASSEMBLE));

        placeHolder.put(PlaceholderKey.$package_dao_impl.name(), classGenerator.getPackage( ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$package_service.name(), classGenerator.getPackage( ClassKey.SERVICE));
        placeHolder.put(PlaceholderKey.$package_controller.name(), classGenerator.getPackage( ClassKey.CONTROLLER));

        placeHolder.put(PlaceholderKey.$class_po.name(), classGenerator.getClassName( ClassKey.PO));
        placeHolder.put(PlaceholderKey.$class_dao.name(), classGenerator.getClassName( ClassKey.DAO));
        placeHolder.put(PlaceholderKey.$class_impl_dao.name(), classGenerator.getClassName( ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$class_service.name(), classGenerator.getClassName( ClassKey.SERVICE));

        placeHolder.put(PlaceholderKey.$class_repository.name(), classGenerator.getClassName( ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$class_repositoryImpl.name(), classGenerator.getClassName( ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$class_controller.name(), classGenerator.getClassName( ClassKey.CONTROLLER));

        placeHolder.put(PlaceholderKey.$object_dao.name(), StringUtility.setFirstByteLowerCase(placeHolder.get(PlaceholderKey.$class_dao.name())));

        placeHolder.put(PlaceholderKey.$object_service.name(), StringUtility.setFirstByteLowerCase(placeHolder.get(PlaceholderKey.$class_service.name())));

        Field primary = entityManager.getPrimary();
        placeHolder.put(PlaceholderKey.$primary_property_name.name(), primary.getColumnName());
        placeHolder.put(PlaceholderKey.$upper_primary_property_name.name(), StringUtility.setFirstByteUpperCase(primary.getPropertyName()));
        placeHolder.put(PlaceholderKey.$primary_type.name(), primary.getType().getSimpleName());
        placeHolder.put(PlaceholderKey.$sql_insert.name(), entityManager.getInsert());
        placeHolder.put(PlaceholderKey.$sql_delete.name(), entityManager.getDelete());
        placeHolder.put(PlaceholderKey.$sql_update.name(), entityManager.getUpdate());
        placeHolder.put(PlaceholderKey.$field_list.name(), entityManager.getFields());
        if (entityManager.getStatus() != null) {
            String setStatusDefault = String.format("%s.setStatus(StatusRecord.ENABLE)", placeHolder.get(PlaceholderKey.$persistence_object_name.name()));
            String setB2V = String.format("%s.setStatus(bo.getStatus().name())", placeHolder.get(PlaceholderKey.$persistence_object_name.name()));

            placeHolder.put(PlaceholderKey.$set_status_default.name(), setStatusDefault);
            placeHolder.put(PlaceholderKey.$set_status_b2v.name(), setB2V);
        } else {
            placeHolder.put(PlaceholderKey.$set_status_default.name(), "");
            placeHolder.put(PlaceholderKey.$set_status_b2v.name(), "");
        }


        Map<String, Field> fields = entityManager.getPropertyFieldMap();
        StringBuilder fieldBuild = new StringBuilder();
        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            fieldBuild.append(String.format("private %s %s; \n", fieldClazz.getSimpleName(), field.getPropertyName()));
        }
        placeHolder.put(PlaceholderKey.$get_sets.name(), fieldBuild.toString());
        return placeHolder;
    }

    @Override
    public void generate(Long projectId,String tableName) throws IOException {
        ClassGenerator classGenerator = new DefaultClassGenerator(this.registry,projectId,tableName);
        TableContext context = this.registry.getTableContext(projectId,tableName);
        TableConfig tableConfig = context.getTableConfig();
        if (CodeSource.UPLOAD.name().equals(tableConfig.getSource())) {
            classGenerator.generate(ClassKey.PO);
        }
        classGenerator.generate(ClassKey.BO);
        classGenerator.generate(ClassKey.QUERY);
        classGenerator.generate( ClassKey.PARAM);
        classGenerator.generate( ClassKey.VO);
        classGenerator.generate( ClassKey.DAO);
        classGenerator.generate( ClassKey.DAO_IMPL);
        classGenerator.generate( ClassKey.DAO_MYBATIS);
        classGenerator.generate( ClassKey.DATA_CONVERTER);
        classGenerator.generate( ClassKey.SERVICE);
        classGenerator.generate( ClassKey.REPOSITORY);
        classGenerator.generate( ClassKey.REPOSITORY_IMPL);
        classGenerator.generate( ClassKey.ASSEMBLE);
        classGenerator.generate( ClassKey.CONTROLLER);
        classGenerator.generate( ClassKey.PAGER_QUERY);
    }

    @Override
    public void initScaffold() {
        ScaffoldCopier.copy(registry,1L);
    }
}
