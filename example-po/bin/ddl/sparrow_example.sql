DROP TABLE IF EXISTS `sparrow_example`;
CREATE TABLE `sparrow_example` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `avatar` varchar(256) DEFAULT '' COMMENT '头象'  ,
 `user_name` varchar(32) DEFAULT '' COMMENT '用户名'  ,
 `password` varchar(32) DEFAULT '' COMMENT '密码'  ,
 `age` int(10) UNSIGNED DEFAULT 0 COMMENT '年龄'  ,
 `confirm_password` varchar(32) DEFAULT '' COMMENT '确认密码'  ,
 `email` varchar(256) DEFAULT '' COMMENT 'email'  ,
 `id_card` varchar(32) DEFAULT '' COMMENT '身份证'  ,
 `mobile` varchar(16) DEFAULT '' COMMENT '手机号'  ,
 `tel` varchar(16) DEFAULT '' COMMENT '联系电话'  ,
 `name` varchar(16) DEFAULT '' COMMENT '用户姓名'  ,
 `create_user_id` int(11) UNSIGNED  DEFAULT 0 COMMENT '创建人ID'  NOT NULL,
 `gmt_create` bigint(11)  DEFAULT 0 COMMENT '创建时间'  NOT NULL,
 `modified_user_id` int(11) unsigned  DEFAULT 0 COMMENT '更新人ID'  NOT NULL,
 `gmt_modified` bigint(11)  DEFAULT 0 COMMENT '更新时间'  NOT NULL,
 `create_user_name` varchar(64)  DEFAULT '' COMMENT '创建人'  NOT NULL,
 `modified_user_name` varchar(64)  DEFAULT '' COMMENT '更新人'  NOT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='sparrow_example';
