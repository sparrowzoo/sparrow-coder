package com.sparrowzoo.coder.enums;

public enum PlaceholderKey {
    /**
     * e.g. @author harry
     */
    $author,

    /**
     * 代码生成器的家目录
     */
    $coder_home,

    /**
     * 项目的工作目录
     */
    $workspace,

    /**
     * 项目名
     */
    $project,

    $project_i18n,
    /**
     * origin table name e.g user_address
     */
    $origin_table_name,

    $primary_key,

    /**
     * e.g UserAddress
     */
    $persistence_class_name,

    /**
     * first case lower table name e.g userAddress
     */
    $persistence_object_name,

    /**
     * 示例  user-address
     */
    $persistence_object_by_horizontal,

    /**
     * 示例 user/address
     */
    $persistence_object_by_slash,

    /**
     * 示例 user.address
     */
    $persistence_object_by_dot,

    /**
     * 项目
     */
    $module_prefix,

    $package_scan_base,

    /**
     * po: e.g com.sparrow.user.po
     */
    $package_po,

    /**
     * po: e.g com.sparrow.user.bo
     */
    $package_bo,

    /**
     * po: e.g com.sparrow.user.param
     */
    $package_param,

    /**
     * po: e.g com.sparrow.user.query
     */
    $package_query,

    /**
     * po: e.g com.sparrow.user.dto
     */
    $package_dto,

    /**
     * po: e.g com.sparrow.user.vo
     */
    $package_vo,

    /**
     * dao e.g com.sparrow.user.dao
     */
    $package_dao,

    $package_pager_query,


    /**
     * dao impl e.g com.sparrow.user.dao.impl
     */
    $package_dao_impl,

    /**
     * dao e.g com.sparrow.user.repository
     */
    $package_repository,

    /**
     * dao impl e.g com.sparrow.user.dao.repository.impl
     */
    $package_repository_impl,

    $package_data_converter,

    $package_assemble,

    /**
     * service e.g com.sparrow.user.service
     */
    $package_service,

    /**
     * controller e.g com.sparrow.user.controller
     */
    $package_controller,

    /**
     * User
     */
    $class_po,

    /**
     * UserDao
     */
    $class_dao,

    /**
     * UserService
     */
    $class_service,

    /**
     * Repository
     */
    $class_repository,

    /**
     * Repository impl
     */
    $class_repositoryImpl,

    /**
     * UserDaoImpl
     */
    $class_impl_dao,

    /**
     * UserController
     */
    $class_controller,

    $package_batch_param,

    /**
     * primary property name e.g userId
     */
    $primary_property_name,

    /**
     * e.g UserId
     */
    $upper_primary_property_name,

    $primary_type,

    /**
     * userDao
     */
    $object_dao,

    /**
     * userService
     */
    $object_service,

    /**
     * current date 2018-11-15
     */
    $date,

    /*for mybatis */

    $result_map,

    $sql_insert,

    $sql_query_one,
    $page_size,

    $sql_update,
    $sql_delete,
    $field_list,
    $status_field,
    $get_sets,
    $get_sets_display_text,
    $get_sets_params,
    $init_po,
    $service_kvs,
    $dictionary_import,
    $dictionary_inject,
    $dictionaries,

    $search_fields,
    $search_dao_condition,


    $frontend_path_page,
    $frontend_path_api,
    $frontend_path_add,
    $frontend_path_edit,
    $frontend_path_search,
    $frontend_path_operation,
    $frontend_path_columns,
    $frontend_path_schema,
    $frontend_class,
    $frontend_column_defs,
    $frontend_column_import,
    $frontend_schema,
    $frontend_add_form_items,
    $frontend_edit_form_items,
    $frontend_i18n_message,

    $frontend_query_fields,
    $frontend_search_items
}
