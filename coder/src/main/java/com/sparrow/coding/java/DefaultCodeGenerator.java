package com.sparrow.coding.java;

import com.sparrow.coding.api.CodeGenerator;
import com.sparrow.coding.api.TableConfigRegistry;
import com.sparrow.coding.api.backend.ClassGenerator;
import com.sparrow.coding.enums.ClassKey;
import com.sparrow.coding.enums.CodeSource;
import com.sparrow.coding.java.enums.PlaceholderKey;
import com.sparrow.coding.po.ProjectConfig;
import com.sparrow.coding.po.TableConfig;
import com.sparrow.coding.protocol.ColumnDef;
import com.sparrow.coding.protocol.TableContext;
import com.sparrow.container.ConfigReader;
import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.utility.DateTimeUtility;
import com.sparrow.utility.StringUtility;

import java.io.IOException;
import java.util.*;


public class DefaultCodeGenerator implements CodeGenerator {

    public DefaultCodeGenerator(Long projectId) throws IOException, ClassNotFoundException {
        this.initRegistry(projectId);
    }

    private TableConfigRegistry registry = new TableConfigRegistry();

    @Override
    public void initRegistry(Long projectId) throws ClassNotFoundException, IOException {
        ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setId(1L);
        projectConfig.setName("sparrow-coder");
        projectConfig.setDescription("授权");
        projectConfig.setCodeTemplate("template");
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
        projectConfig.setCreateUserName("");
        projectConfig.setModifiedUserName("");
        projectConfig.setWrapWithParent(false);

        this.registry.register(projectConfig);
        TableConfig tableConfig = new TableConfig();
        tableConfig.setId(0L);
        tableConfig.setProjectId(1L);
        tableConfig.setPrimaryKey("id");
        tableConfig.setTableName("t_table_config");
        tableConfig.setClassName("com.sparrowzoo.coder.TableConfig");
        tableConfig.setDescription("");
        tableConfig.setCheckable(false);
        tableConfig.setRowMenu(false);
        tableConfig.setColumnFilter(false);
        tableConfig.setStatusCommand(false);
        tableConfig.setColumnConfigs("");
        tableConfig.setSource(CodeSource.INNER.name());
        tableConfig.setSourceCode("");
        tableConfig.setColumnDefs(new ArrayList<ColumnDef>());
        tableConfig.setCreateUserId(0L);
        tableConfig.setModifiedUserId(0L);
        tableConfig.setGmtCreate(0L);
        tableConfig.setGmtModified(0L);
        tableConfig.setCreateUserName("");
        tableConfig.setModifiedUserName("");

        TableContext context = new TableContext();
        context.setPoPackage("com.sparrow.coding.po");
        context.setTableConfig(tableConfig);
        Container container = ApplicationContext.getContainer();
        ConfigReader reader = container.getBean(ConfigReader.class);
        this.registry.setWorkspace(reader.getValue("workspace"));
        this.registry.setScaffold(reader.getValue("scaffold"));
        this.registry.register(tableConfig.getTableName(), context);
        this.initPlaceHolder(context);
    }


    private Map<String, String> initPlaceHolder(TableContext tableContext) throws IOException, ClassNotFoundException {
        Map<String, String> placeHolder = new TreeMap<>(Comparator.reverseOrder());
        tableContext.setPlaceHolder(placeHolder);

        ProjectConfig projectConfig = this.registry.getProjectConfig();
        TableConfig tableConfig = tableContext.getTableConfig();
        EntityManager entityManager = new SparrowEntityManager(Class.forName(tableConfig.getClassName()));
        String tableName = entityManager.getTableName();
        String persistenceClassName = entityManager.getSimpleClassName();
        String modulePrefix = projectConfig.getModulePrefix();
        ClassGenerator classGenerator = new DefaultClassGenerator(this.registry);
        placeHolder.put(PlaceholderKey.$module_prefix.name(), modulePrefix);
        placeHolder.put(PlaceholderKey.$origin_table_name.name(), tableName);
        placeHolder.put(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
        placeHolder.put(PlaceholderKey.$persistence_object_name.name(), StringUtility.setFirstByteLowerCase(persistenceClassName));
        placeHolder.put(PlaceholderKey.$persistence_object_by_horizontal.name(), StringUtility.humpToLower(persistenceClassName, '-'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_slash.name(), StringUtility.humpToLower(persistenceClassName, '/'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_dot.name(), StringUtility.humpToLower(persistenceClassName, '.'));

        placeHolder.put(PlaceholderKey.$date.name(), DateTimeUtility
                .getFormatCurrentTime("yyyy-MM-dd HH:mm:ss"));

        placeHolder.put(PlaceholderKey.$package_po.name(), tableContext.getPoPackage());
        placeHolder.put(PlaceholderKey.$package_bo.name(), classGenerator.getPackage(tableName, ClassKey.BO));
        placeHolder.put(PlaceholderKey.$package_param.name(), classGenerator.getPackage(tableName, ClassKey.PARAM));

        placeHolder.put(PlaceholderKey.$package_batch_param.name(), classGenerator.getPackage(tableName, ClassKey.BATCH_OPERATE_PARAM));

        placeHolder.put(PlaceholderKey.$package_query.name(), classGenerator.getPackage(tableName, ClassKey.QUERY));
        placeHolder.put(PlaceholderKey.$package_dto.name(), classGenerator.getPackage(tableName, ClassKey.DTO));
        placeHolder.put(PlaceholderKey.$package_vo.name(), classGenerator.getPackage(tableName, ClassKey.VO));

        placeHolder.put(PlaceholderKey.$package_dao.name(), classGenerator.getPackage(tableName, ClassKey.DAO));
        String pagerQuery = classGenerator.getPackage(tableName, ClassKey.PAGER_QUERY);
        placeHolder.put(PlaceholderKey.$package_pager_query.name(), StringUtility.replace(pagerQuery, placeHolder));
        placeHolder.put(PlaceholderKey.$package_repository.name(), classGenerator.getPackage(tableName, ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$package_repository_impl.name(), classGenerator.getPackage(tableName, ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$package_data_converter.name(), classGenerator.getPackage(tableName, ClassKey.DATA_CONVERTER));
        placeHolder.put(PlaceholderKey.$package_assemble.name(), classGenerator.getPackage(tableName, ClassKey.ASSEMBLE));

        placeHolder.put(PlaceholderKey.$package_dao_impl.name(), classGenerator.getPackage(tableName, ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$package_service.name(), classGenerator.getPackage(tableName, ClassKey.SERVICE));
        placeHolder.put(PlaceholderKey.$package_controller.name(), classGenerator.getPackage(tableName, ClassKey.CONTROLLER));

        placeHolder.put(PlaceholderKey.$class_po.name(), classGenerator.getClassName(tableName, ClassKey.PO));
        placeHolder.put(PlaceholderKey.$class_dao.name(), classGenerator.getClassName(tableName, ClassKey.DAO));
        placeHolder.put(PlaceholderKey.$class_impl_dao.name(),
                classGenerator.getClassName(tableName, ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$class_service.name(),
                classGenerator.getClassName(tableName, ClassKey.SERVICE));

        placeHolder.put(PlaceholderKey.$class_repository.name(), classGenerator.getClassName(tableName, ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$class_repositoryImpl.name(), classGenerator.getClassName(tableName, ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$class_controller.name(), classGenerator.getClassName(tableName, ClassKey.CONTROLLER));

        placeHolder.put(PlaceholderKey.$object_dao.name(),
                StringUtility.setFirstByteLowerCase(placeHolder.get(PlaceholderKey.$class_dao.name())));

        placeHolder.put(PlaceholderKey.$object_service.name(),
                StringUtility.setFirstByteLowerCase(placeHolder.get(PlaceholderKey.$class_service.name())));

        Field primary = entityManager.getPrimary();
        placeHolder.put(PlaceholderKey.$primary_property_name.name(), primary.getColumnName());
        placeHolder.put(PlaceholderKey.$upper_primary_property_name.name(), StringUtility.setFirstByteUpperCase(primary.getPropertyName()));
        placeHolder.put(PlaceholderKey.$primary_type.name(), primary.getType().getSimpleName());
        placeHolder.put(PlaceholderKey.$sql_insert.name(), entityManager.getInsert());
        placeHolder.put(PlaceholderKey.$sql_delete.name(), entityManager.getDelete());
        placeHolder.put(PlaceholderKey.$sql_update.name(), entityManager.getUpdate());
        placeHolder.put(PlaceholderKey.$field_list.name(), entityManager.getFields());

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
    public void generate(String tableName) throws IOException {
        ClassGenerator classGenerator = new DefaultClassGenerator(this.registry);
        TableContext context = this.registry.getTableContext(tableName);
        TableConfig tableConfig = context.getTableConfig();
        if (CodeSource.INNER.name().equals(tableConfig.getSource())) {
            classGenerator.generate(tableName, ClassKey.PO);
        }
        classGenerator.generate(tableName, ClassKey.BO);
        classGenerator.generate(tableName, ClassKey.QUERY);
        classGenerator.generate(tableName, ClassKey.PARAM);
        classGenerator.generate(tableName, ClassKey.VO);
        classGenerator.generate(tableName, ClassKey.DAO);
        classGenerator.generate(tableName, ClassKey.DAO_IMPL);
        classGenerator.generate(tableName, ClassKey.DAO_MYBATIS);
        classGenerator.generate(tableName, ClassKey.DATA_CONVERTER);
        classGenerator.generate(tableName, ClassKey.SERVICE);
        classGenerator.generate(tableName, ClassKey.REPOSITORY);
        classGenerator.generate(tableName, ClassKey.REPOSITORY_IMPL);
        classGenerator.generate(tableName, ClassKey.ASSEMBLE);
        classGenerator.generate(tableName, ClassKey.CONTROLLER);
        classGenerator.generate(tableName, ClassKey.PAGER_QUERY);
    }

    @Override
    public void initScaffold() {
        ScaffoldCopier.copy(registry);
    }
}
