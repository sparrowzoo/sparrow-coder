package com.sparrowzoo.coder.domain.service.backend;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.utility.DateTimeUtility;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.constant.Config;
import com.sparrowzoo.coder.domain.bo.ProjectBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.enums.ClassKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Properties;

@Slf4j
public class DefaultClassPlaceholder implements ClassPlaceholderGenerator {
    protected final ProjectBO project;
    protected final TableContext tableContext;
    protected final Properties scaffoldConfig;
    protected final FileUtility fileUtility = FileUtility.getInstance();

    public DefaultClassPlaceholder(ProjectBO project,TableContext tableContext){
        this.project = project;
        this.scaffoldConfig = project.getScaffoldConfig();
        this.tableContext =tableContext;
        this.init();
    }

    public String getModule(ClassKey key) {
        String parentModule = "admin";
        String moduleKey = Config.MODULE_PREFIX;
        if (!ClassKey.PO.equals(key)) {
            moduleKey += parentModule + "." + key.getModule().toLowerCase();
        } else {
            moduleKey += key.getModule().toLowerCase();
        }
        String modulePath = this.scaffoldConfig.getProperty(moduleKey);
        if (modulePath == null) {
            log.error("module path is null, module key is [{}]", moduleKey);
            return "";
        }
        return modulePath.replace(PlaceholderKey.$module_prefix.name(), this.project.getProjectConfig().getModulePrefix());
    }
    @Override
    public String getPackage(ClassKey classKey) {
        String packageName = this.scaffoldConfig.getProperty(Config.PACKAGE_PREFIX + classKey.name().toLowerCase());
        if (packageName == null) {
            return "";
        }
        if (!this.project.getProjectConfig().getWrapWithParent()) {
            packageName = packageName.replaceAll("admin.", "");
        }
        String poPackage = this.tableContext.getPoPackage();
        return fileUtility.replacePath(poPackage, ClassKey.PO.name(), packageName, Symbol.DOT);
    }


    public String getClassName(ClassKey classKey) {
        String originClassName = this.scaffoldConfig.getProperty(Config.CLASS_PREFIX + classKey.name().toLowerCase());
        String persistenceClassName =tableContext.getEntityManager().getSimpleClassName();
        return originClassName.replace(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
    }

    public void init() {
        Map<String, String> placeHolder=tableContext.getPlaceHolder();
        EntityManager entityManager=tableContext.getEntityManager();
        String tableName = entityManager.getTableName();
        String persistenceClassName = entityManager.getSimpleClassName();
        String modulePrefix = this.project.getProjectConfig().getModulePrefix();
        placeHolder.put(PlaceholderKey.$package_bo.name(), this.getPackage(ClassKey.BO));
        placeHolder.put(PlaceholderKey.$package_param.name(), this.getPackage(ClassKey.PARAM));
        placeHolder.put(PlaceholderKey.$package_query.name(), this.getPackage(ClassKey.QUERY));
        placeHolder.put(PlaceholderKey.$package_dto.name(), this.getPackage(ClassKey.DTO));
        placeHolder.put(PlaceholderKey.$package_vo.name(), this.getPackage(ClassKey.VO));
        placeHolder.put(PlaceholderKey.$package_dao.name(), this.getPackage(ClassKey.DAO));
        placeHolder.put(PlaceholderKey.$package_pager_query.name(),this.getPackage(ClassKey.PAGER_QUERY));
        placeHolder.put(PlaceholderKey.$package_repository.name(), this.getPackage(ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$package_repository_impl.name(), this.getPackage(ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$package_data_converter.name(), this.getPackage(ClassKey.DATA_CONVERTER));
        placeHolder.put(PlaceholderKey.$package_assemble.name(), this.getPackage(ClassKey.ASSEMBLE));
        placeHolder.put(PlaceholderKey.$package_dao_impl.name(), this.getPackage(ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$package_service.name(), this.getPackage(ClassKey.SERVICE));
        placeHolder.put(PlaceholderKey.$package_controller.name(), this.getPackage(ClassKey.CONTROLLER));
        placeHolder.put(PlaceholderKey.$class_po.name(), this.getClassName(ClassKey.PO));
        placeHolder.put(PlaceholderKey.$class_dao.name(), this.getClassName(ClassKey.DAO));
        placeHolder.put(PlaceholderKey.$class_impl_dao.name(), this.getClassName(ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$class_service.name(), this.getClassName(ClassKey.SERVICE));
        placeHolder.put(PlaceholderKey.$class_repository.name(), this.getClassName(ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$class_repositoryImpl.name(), this.getClassName(ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$class_controller.name(), this.getClassName(ClassKey.CONTROLLER));
        placeHolder.put(PlaceholderKey.$object_dao.name(), StringUtility.setFirstByteLowerCase(placeHolder.get(PlaceholderKey.$class_dao.name())));
        placeHolder.put(PlaceholderKey.$object_service.name(), StringUtility.setFirstByteLowerCase(placeHolder.get(PlaceholderKey.$class_service.name())));

        placeHolder.put(PlaceholderKey.$module_prefix.name(), modulePrefix);
        placeHolder.put(PlaceholderKey.$primary_key.name(), entityManager.getPrimary().getPropertyName());
        placeHolder.put(PlaceholderKey.$origin_table_name.name(), tableName);
        placeHolder.put(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
        placeHolder.put(PlaceholderKey.$persistence_object_name.name(), StringUtility.setFirstByteLowerCase(persistenceClassName));
        placeHolder.put(PlaceholderKey.$persistence_object_by_horizontal.name(), StringUtility.humpToLower(persistenceClassName, '-'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_slash.name(), StringUtility.humpToLower(persistenceClassName, '/'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_dot.name(), StringUtility.humpToLower(persistenceClassName, '.'));
        placeHolder.put(PlaceholderKey.$date.name(), DateTimeUtility.getFormatCurrentTime("yyyy-MM-dd HH:mm:ss"));
        placeHolder.put(PlaceholderKey.$package_po.name(), tableContext.getPoPackage());
        placeHolder.put(PlaceholderKey.$package_scan_base.name(), tableContext.getPoPackage().replace("." + ClassKey.PO.name().toLowerCase(), ""));
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
    }
}
