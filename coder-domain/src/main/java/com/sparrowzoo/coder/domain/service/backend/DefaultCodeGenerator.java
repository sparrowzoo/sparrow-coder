package com.sparrowzoo.coder.domain.service.backend;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.orm.SparrowEntityManager;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.DateTimeUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.DomainRegistry;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.CodeGenerator;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.enums.ClassKey;
import com.sparrowzoo.coder.enums.CodeSource;
import com.sparrowzoo.coder.enums.PlaceholderKey;
import com.sparrowzoo.coder.po.ProjectConfig;
import com.sparrowzoo.coder.po.TableConfig;
import com.sparrowzoo.coder.domain.service.EnvConfig;
import com.sparrowzoo.coder.domain.service.registry.ArchitectureRegistry;
import com.sparrowzoo.coder.protocol.query.ProjectConfigQuery;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class DefaultCodeGenerator implements CodeGenerator {
    private TableConfigRegistry registry;
    private DomainRegistry domainRegistry;

    public DefaultCodeGenerator(Long projectId, EnvConfig envConfig, DomainRegistry domainRegistry) throws IOException, ClassNotFoundException {
        this.registry = new TableConfigRegistry();
        ProjectConfigBO projectConfig = domainRegistry.getProjectConfigRepository().getProjectConfig(projectId);
        this.registry.setProject(projectConfig);
        this.registry.setEnvConfig(envConfig);
        this.domainRegistry = domainRegistry;
        this.initRegistry(registry);
    }


    @Override
    public void initRegistry(TableConfigRegistry registry) throws ClassNotFoundException, IOException {
        TableConfigQuery projectConfigQuery = new TableConfigQuery();
        projectConfigQuery.setProjectId(registry.getProject().getId());
        projectConfigQuery.setDeleted(false);
        projectConfigQuery.setStatus(StatusRecord.ENABLE.ordinal());
        List<TableConfigBO> tableConfigs = domainRegistry.getTableConfigRepository().queryTableConfigs(projectConfigQuery);
        for (TableConfigBO tableConfigBO : tableConfigs) {
            TableContext context = new TableContext();
            context.setTableConfig(tableConfigBO);
            this.registry.register(tableConfigBO.getTableName(), context);
            this.initPlaceHolder(context);
        }
    }


    private Map<String, String> initPlaceHolder(TableContext tableContext) throws IOException, ClassNotFoundException {
        Map<String, String> placeHolder = new TreeMap<>(Comparator.reverseOrder());
        tableContext.setPlaceHolder(placeHolder);
        ProjectConfigBO projectConfig = this.registry.getProjectConfig();
        TableConfigBO tableConfig = tableContext.getTableConfig();
        EntityManager entityManager = new SparrowEntityManager(Class.forName(tableConfig.getClassName()));
        tableContext.setEntityManager(entityManager);
        String tableName = entityManager.getTableName();
        String persistenceClassName = entityManager.getSimpleClassName();
        String modulePrefix = projectConfig.getModulePrefix();
        ClassGenerator classGenerator = new DefaultClassGenerator(this.registry, tableConfig.getTableName());
        placeHolder.put(PlaceholderKey.$module_prefix.name(), modulePrefix);
        placeHolder.put(PlaceholderKey.$origin_table_name.name(), tableName);
        placeHolder.put(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
        placeHolder.put(PlaceholderKey.$persistence_object_name.name(), StringUtility.setFirstByteLowerCase(persistenceClassName));
        placeHolder.put(PlaceholderKey.$persistence_object_by_horizontal.name(), StringUtility.humpToLower(persistenceClassName, '-'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_slash.name(), StringUtility.humpToLower(persistenceClassName, '/'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_dot.name(), StringUtility.humpToLower(persistenceClassName, '.'));
        placeHolder.put(PlaceholderKey.$date.name(), DateTimeUtility.getFormatCurrentTime("yyyy-MM-dd HH:mm:ss"));
        placeHolder.put(PlaceholderKey.$package_po.name(), tableContext.getPoPackage());
        placeHolder.put(PlaceholderKey.$package_scan_base.name(), tableContext.getPoPackage().replace("." + ClassKey.PO.name().toLowerCase(), ""));
        placeHolder.put(PlaceholderKey.$package_bo.name(), classGenerator.getPackage(ClassKey.BO));
        placeHolder.put(PlaceholderKey.$package_param.name(), classGenerator.getPackage(ClassKey.PARAM));
        placeHolder.put(PlaceholderKey.$package_query.name(), classGenerator.getPackage(ClassKey.QUERY));
        placeHolder.put(PlaceholderKey.$package_dto.name(), classGenerator.getPackage(ClassKey.DTO));
        placeHolder.put(PlaceholderKey.$package_vo.name(), classGenerator.getPackage(ClassKey.VO));

        placeHolder.put(PlaceholderKey.$package_dao.name(), classGenerator.getPackage(ClassKey.DAO));
        String pagerQuery = classGenerator.getPackage(ClassKey.PAGER_QUERY);
        placeHolder.put(PlaceholderKey.$package_pager_query.name(), StringUtility.replace(pagerQuery, placeHolder));
        placeHolder.put(PlaceholderKey.$package_repository.name(), classGenerator.getPackage(ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$package_repository_impl.name(), classGenerator.getPackage(ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$package_data_converter.name(), classGenerator.getPackage(ClassKey.DATA_CONVERTER));
        placeHolder.put(PlaceholderKey.$package_assemble.name(), classGenerator.getPackage(ClassKey.ASSEMBLE));

        placeHolder.put(PlaceholderKey.$package_dao_impl.name(), classGenerator.getPackage(ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$package_service.name(), classGenerator.getPackage(ClassKey.SERVICE));
        placeHolder.put(PlaceholderKey.$package_controller.name(), classGenerator.getPackage(ClassKey.CONTROLLER));

        placeHolder.put(PlaceholderKey.$class_po.name(), classGenerator.getClassName(ClassKey.PO));
        placeHolder.put(PlaceholderKey.$class_dao.name(), classGenerator.getClassName(ClassKey.DAO));
        placeHolder.put(PlaceholderKey.$class_impl_dao.name(), classGenerator.getClassName(ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$class_service.name(), classGenerator.getClassName(ClassKey.SERVICE));

        placeHolder.put(PlaceholderKey.$class_repository.name(), classGenerator.getClassName(ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$class_repositoryImpl.name(), classGenerator.getClassName(ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$class_controller.name(), classGenerator.getClassName(ClassKey.CONTROLLER));

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
        if (entityManager.getPoPropertyNames() != null) {
            String initPO = String.format("POInitUtils.init(%s);\n", placeHolder.get(PlaceholderKey.$persistence_object_name.name()));
            placeHolder.put(PlaceholderKey.$init_po.name(), initPO);
        } else {
            placeHolder.put(PlaceholderKey.$init_po.name(), "");
        }


        Map<String, Field> fields = entityManager.getPropertyFieldMap();
        StringBuilder fieldBuild = new StringBuilder();
        StringBuilder paramFieldBuild = new StringBuilder();
        String statusProperty = null;
        for (Field field : fields.values()) {
            Class<?> fieldClazz = field.getType();
            String property = String.format("private %s %s; \n", fieldClazz.getSimpleName(), field.getPropertyName());
            fieldBuild.append(property);
            if (entityManager.getPoPropertyNames() != null && !entityManager.getPoPropertyNames().contains(field.getPropertyName())) {
                paramFieldBuild.append(property);
            }
        }
        placeHolder.put(PlaceholderKey.$get_sets.name(), fieldBuild.toString());
        placeHolder.put(PlaceholderKey.$get_sets_params.name(), paramFieldBuild.toString());
        return placeHolder;
    }

    @Override
    public void generate(String tableName) throws IOException {
        String architectures = registry.getProjectConfig().getArchitectures();
        ArchitectureRegistry.getInstance().getRegistry()
                .get(ArchitectureCategory.BACKEND)
                .get("clearArchitectureGenerator").generate(registry, tableName);

        ArchitectureRegistry.getInstance().getRegistry()
                .get(ArchitectureCategory.DATABASE)
                .get("mySqlArchitectureGenerator").generate(registry, tableName);

    }

    @Override
    public void initScaffold() {
        ScaffoldCopier.copy(registry);
    }

    @Override
    public void clear() {
        String targetDirectoryPath =
                new FileNameBuilder(registry.getEnvConfig().getWorkspace())
                        .joint(String.valueOf(registry.getProjectConfig().getCreateUserId()))
                        .joint(registry.getProjectConfig().getName())
                        .build();
        FileUtility.getInstance().delete(targetDirectoryPath, System.currentTimeMillis());
    }
}
