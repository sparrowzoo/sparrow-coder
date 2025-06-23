DROP TABLE IF EXISTS `t_project_config`;
CREATE TABLE `t_project_config` (
 `id` int NOT NULL AUTO_INCREMENT,
 `name` varchar(50) default '' comment '项目名称'  NOT NULL,
 `frontend_name` varchar(50) default '' comment '前端项目名称'  ,
 `chinese_name` varchar(50) default '' comment '项目中文名称'  ,
 `i18n` tinyint(1) default 0 comment '是否支持国际化'  ,
 `description` varchar(512) default '' comment '项目描述'  ,
 `module_prefix` varchar(50) default '' comment '模块前缀'  ,
 `scan_package` varchar(512) default '' comment '扫描的包路径'  ,
 `architectures` varchar(50) default '' comment '代码架构'  ,
 `config` varchar(512) default '' comment '脚手架配置'  ,
 `wrap_with_parent` tinyint(1) default 0 comment '是否使用父module'  ,
 `scaffold` varchar(50) default '' comment '脚手架'  ,
 `create_user_name` varchar(64)  DEFAULT '' COMMENT '创建人'  NOT NULL,
 `create_user_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '创建人ID'  NOT NULL,
 `modified_user_id` int(11) unsigned  DEFAULT 0 COMMENT '更新人ID'  NOT NULL,
 `modified_user_name` varchar(64)  DEFAULT '' COMMENT '更新人'  NOT NULL,
 `gmt_create` bigint(11)  DEFAULT 0 COMMENT '创建时间'  NOT NULL,
 `gmt_modified` bigint(11)  DEFAULT 0 COMMENT '更新时间'  NOT NULL,
 `deleted` tinyint(1)  DEFAULT 0 COMMENT '是否删除'  NOT NULL,
 `status` tinyint(3) UNSIGNED DEFAULT 0 COMMENT 'STATUS'  NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='t_project_config';
