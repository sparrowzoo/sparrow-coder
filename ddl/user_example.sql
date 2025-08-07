DROP TABLE IF EXISTS `t_user_example`;
CREATE TABLE `t_user_example` (
 `id` int COMMENT 'ID' NOT NULL AUTO_INCREMENT,
 `user_name` varchar(32) COMMENT '用户名'  NOT NULL,
 `chinese_name` varchar(32)  default '' comment '中文名'  NOT NULL,
 `birthday` date null comment '出生日期'  NOT NULL,
 `email` varchar(128)  default '' comment 'Email'  NOT NULL,
 `mobile` varchar(128)  default '' comment '手机号'  NOT NULL,
 `tel` varchar(128)  default '' comment '电话号码'  NOT NULL,
 `id_card` varchar(128)  default '' comment '身份证'  NOT NULL,
 `gender` int not null default 999 comment '性别'  NOT NULL,
 `age` int not null default 0 comment '年龄'  NOT NULL,
 `create_user_name` varchar(64)  DEFAULT '' COMMENT '创建人'  NOT NULL,
 `create_user_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '创建人ID'  NOT NULL,
 `modified_user_id` int(11) unsigned  DEFAULT 0 COMMENT '更新人ID'  NOT NULL,
 `modified_user_name` varchar(64)  DEFAULT '' COMMENT '更新人'  NOT NULL,
 `gmt_create` bigint(11)  DEFAULT 0 COMMENT '创建时间'  NOT NULL,
 `gmt_modified` bigint(11)  DEFAULT 0 COMMENT '更新时间'  NOT NULL,
 `deleted` tinyint(1)  DEFAULT 0 COMMENT '是否删除'  NOT NULL,
 `status` tinyint(3) UNSIGNED DEFAULT 0 COMMENT '状态'  NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='t_user_example';
