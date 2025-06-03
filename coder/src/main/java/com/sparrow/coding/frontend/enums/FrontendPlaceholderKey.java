package com.sparrow.coding.frontend.enums;

/**
 * 前缀配置的placeholder
 */
public enum FrontendPlaceholderKey {

    /**
     * 实体对象名称 比如:用户对象名 user
     */
    $entity_name,
    /**
     * 实体对象中文描述 比如 用户
     */
    $entity_text,

    /**
     * "-"转换实体对象  比如example-front
     */
    $entity_by_horizontal,

    /**
     * "/"转换实体对象 比如example/front
     */
    $entity_by_slash,


    /**
     * 主键 key
     */
    $primary_key,
    /**
     * 实体对象名称 比喻:用户对象名 User
     */
    $upper_entity_name,
    /**
     * 字段的中文描述 比如:用户名
     */
    $field_text,
    /**
     * 比如:userName
     */
    $property_name,
    /**
     * 首字段大写的属性名称 比如:UserName
     */
    $upper_property_name,
    /**
     * 在管理显示的宽度 80px
     */
    $width,
    /**
     * 管理页面的头行html
     */
    $manage_header_line,
    /**
     * 管理页面的数据行html
     */
    $manage_data_table,

    /**
     * java 工程工作目录
     */
    $workspace,

    /**
     * 资源文件工程目录
     */
    $resource_workspace,
    /**
     * 项目
     */
    $project
}
