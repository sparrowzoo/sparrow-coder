package com.sparrow.coding.support.enums;

public enum REPLACE_KEY {
    /**
     * e.g. @author harry
     */
    $author,

    /**
     * origin table name
     * e.g a_user
     */
    $origin_table_name,

    /**
     * e.g user or User
     */
    $table_name,

    /**
     * first case lower table name
     * e.g user
     */
    $lower_table_name,

    /**
     * po: e.g com.sparrow.user.po
     */
    $package_po,

    /**
     * dao e.g com.sparrow.user.dao
     */
    $package_dao,

    /**
     * dao impl e.g com.sparrow.user.dao.impl
     */
    $package_impl_dao,

    /**
     * service e.g com.sparrow.user.service
     */
    $package_service,

    /**
     * service impl e.g com.sparrow.user.service.impl
     */
    $package_impl_service,

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
     * UserDaoImpl
     */
    $class_impl_dao,

    /**
     * UserServiceImpl
     */
    $class_impl_service,

    /**
     * UserController
     */
    $class_controller,

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
     * display name e.g 用户名
     */
    $display_name,

    /**
     * object po name e.g
     * private User user (user is object name)
     */
    $object_po,

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

    $sql_update,

    $sql_delete,

    $field_list

}
