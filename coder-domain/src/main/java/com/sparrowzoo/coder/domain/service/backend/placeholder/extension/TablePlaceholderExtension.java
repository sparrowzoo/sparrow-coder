package com.sparrowzoo.coder.domain.service.backend.placeholder.extension;

import com.sparrow.orm.EntityManager;
import com.sparrow.orm.Field;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractPlaceholderExtension;
import com.sparrowzoo.coder.domain.service.backend.ClassPlaceholderGenerator;
import com.sparrowzoo.coder.enums.ClassKey;
import com.sparrowzoo.coder.enums.PlaceholderKey;

import javax.inject.Named;
import java.util.Map;

@Named
public class TablePlaceholderExtension extends AbstractPlaceholderExtension {
    @Override
    public void extend(TableContext tableContext) {
        Map<String, String> placeHolder = tableContext.getPlaceHolder();
        ClassPlaceholderGenerator classPlaceholderGenerator = tableContext.getClassPlaceholderGenerator();
        EntityManager entityManager = tableContext.getEntityManager();
        String tableName = entityManager.getTableName();
        String persistenceClassName = entityManager.getSimpleClassName();
        String persistenceObjectName = StringUtility.setFirstByteLowerCase(persistenceClassName);
        placeHolder.put(PlaceholderKey.$package_bo.name(), classPlaceholderGenerator.getPackage(ClassKey.BO));
        placeHolder.put(PlaceholderKey.$package_param.name(), classPlaceholderGenerator.getPackage(ClassKey.PARAM));
        placeHolder.put(PlaceholderKey.$package_query.name(), classPlaceholderGenerator.getPackage(ClassKey.QUERY));
        placeHolder.put(PlaceholderKey.$package_dto.name(), classPlaceholderGenerator.getPackage(ClassKey.DTO));
        placeHolder.put(PlaceholderKey.$package_vo.name(), classPlaceholderGenerator.getPackage(ClassKey.VO));
        placeHolder.put(PlaceholderKey.$package_dao.name(), classPlaceholderGenerator.getPackage(ClassKey.DAO));
        placeHolder.put(PlaceholderKey.$package_pager_query.name(), classPlaceholderGenerator.getPackage(ClassKey.PAGER_QUERY));
        placeHolder.put(PlaceholderKey.$package_repository.name(), classPlaceholderGenerator.getPackage(ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$package_repository_impl.name(), classPlaceholderGenerator.getPackage(ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$package_data_converter.name(), classPlaceholderGenerator.getPackage(ClassKey.DATA_CONVERTER));
        placeHolder.put(PlaceholderKey.$package_assemble.name(), classPlaceholderGenerator.getPackage(ClassKey.ASSEMBLE));
        placeHolder.put(PlaceholderKey.$package_dao_impl.name(), classPlaceholderGenerator.getPackage(ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$package_service.name(), classPlaceholderGenerator.getPackage(ClassKey.SERVICE));
        placeHolder.put(PlaceholderKey.$package_controller.name(), classPlaceholderGenerator.getPackage(ClassKey.CONTROLLER));
        placeHolder.put(PlaceholderKey.$class_po.name(), classPlaceholderGenerator.getClassName(ClassKey.PO));
        placeHolder.put(PlaceholderKey.$class_dao.name(), classPlaceholderGenerator.getClassName(ClassKey.DAO));
        placeHolder.put(PlaceholderKey.$class_impl_dao.name(), classPlaceholderGenerator.getClassName(ClassKey.DAO_IMPL));
        placeHolder.put(PlaceholderKey.$class_service.name(), classPlaceholderGenerator.getClassName(ClassKey.SERVICE));
        placeHolder.put(PlaceholderKey.$class_repository.name(), classPlaceholderGenerator.getClassName(ClassKey.REPOSITORY));
        placeHolder.put(PlaceholderKey.$class_repositoryImpl.name(), classPlaceholderGenerator.getClassName(ClassKey.REPOSITORY_IMPL));
        placeHolder.put(PlaceholderKey.$class_controller.name(), classPlaceholderGenerator.getClassName(ClassKey.CONTROLLER));
        placeHolder.put(PlaceholderKey.$object_dao.name(), StringUtility.setFirstByteLowerCase(placeHolder.get(PlaceholderKey.$class_dao.name())));
        placeHolder.put(PlaceholderKey.$object_service.name(), StringUtility.setFirstByteLowerCase(placeHolder.get(PlaceholderKey.$class_service.name())));
        Field primary = entityManager.getPrimary();
        if (primary != null) {
            placeHolder.put(PlaceholderKey.$primary_key.name(), primary.getPropertyName());
            placeHolder.put(PlaceholderKey.$primary_property_name.name(), primary.getColumnName());
            placeHolder.put(PlaceholderKey.$primary_type.name(), primary.getType().getSimpleName());
            placeHolder.put(PlaceholderKey.$upper_primary_property_name.name(), StringUtility.setFirstByteUpperCase(primary.getPropertyName()));
        }
        placeHolder.put(PlaceholderKey.$origin_table_name.name(), tableName);
        placeHolder.put(PlaceholderKey.$persistence_class_name.name(), persistenceClassName);
        placeHolder.put(PlaceholderKey.$persistence_object_name.name(), persistenceObjectName);
        placeHolder.put(PlaceholderKey.$persistence_object_by_horizontal.name(), StringUtility.humpToLower(persistenceClassName, '-'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_slash.name(), StringUtility.humpToLower(persistenceClassName, '/'));
        placeHolder.put(PlaceholderKey.$persistence_object_by_dot.name(), StringUtility.humpToLower(persistenceClassName, '.'));
        placeHolder.put(PlaceholderKey.$package_po.name(), tableContext.getPoPackage());
        placeHolder.put(PlaceholderKey.$package_scan_base.name(), tableContext.getPoPackage().replace("." + ClassKey.PO.name().toLowerCase(), ""));
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
    }
}
