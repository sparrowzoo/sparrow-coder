package com.sparrowzoo.coder.po;

import com.sparrow.protocol.dao.PO;
import lombok.Data;

import javax.persistence.*;

@Table(name = "t_project_config")
@Data
public class ProjectConfig extends PO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int")
    private Long id;
    @Column(name = "name", updatable = false, nullable = false, columnDefinition = "varchar(50) default '' comment '项目名称'")
    private String name;
    @Column(name = "frontend_name", columnDefinition = "varchar(50) default '' comment '前端项目名称'")
    private String frontendName;
    @Column(name = "chinese_name", columnDefinition = "varchar(50) default '' comment '项目中文名称'")
    private String chineseName;
    @Column(name = "description", columnDefinition = "varchar(512) default '' comment '项目描述'")
    private String description;
    @Column(name = "module_prefix", columnDefinition = "varchar(50) default '' comment '模块前缀'")
    private String modulePrefix;
    @Column(name = "scan_package", columnDefinition = "varchar(512) default '' comment '扫描的包路径'")
    private String scanPackage;
    @Column(name = "architectures", columnDefinition = "varchar(50) default '' comment '代码架构'")
    private String architectures;
    @Column(name = "config", columnDefinition = "varchar(512) default '' comment '脚手架配置'")
    private String config;
    @Column(name = "wrap_with_parent", columnDefinition = "tinyint(1) default 0 comment '是否使用父module'")
    private Boolean wrapWithParent;
    @Column(name = "implanted", columnDefinition = "tinyint(1) default 0 comment '是否项目本身，内嵌代码生成器'")
    private Boolean implanted;
    @Column(name = "scaffold", columnDefinition = "varchar(50) default '' comment '脚手架'")
    private String scaffold;
}
