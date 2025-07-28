package com.sparrowzoo.coder.po;

import com.sparrow.protocol.dao.ListDatasource;
import com.sparrow.protocol.dao.PO;
import com.sparrow.protocol.dao.enums.ListDatasourceType;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "t_table_config")
public class TableConfig extends PO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int COMMENT 'ID'")
    private Long id;
    @Column(name = "project_id", nullable = false, columnDefinition = "int comment '项目ID'")
    @ListDatasource(type = ListDatasourceType.TABLE,params = "t_project_config")
    private Long projectId;
    @Column(name = "primary_key", updatable = false, nullable = false, columnDefinition = "varchar(32) default '' comment '主键'")
    private String primaryKey;
    @Column(name = "table_name", updatable = false, nullable = false, unique = true, columnDefinition = "varchar(128)  default '' comment '表名'")
    private String tableName;
    @Column(name = "class_name", updatable = false, nullable = false, unique = true, columnDefinition = "varchar(128)  default '' comment '类名'")
    private String className;
    @Column(name = "description", nullable = false, columnDefinition = "varchar(255)  default '' comment '描述'")
    private String description;
    @Column(name = "locked", nullable = false, columnDefinition = "tinyint(1)  default 0 comment '是否锁定'")
    private Boolean locked;
    @Column(name = "checkable", nullable = false, columnDefinition = "tinyint(4)  default 0 comment '选择'")
    private Integer checkable;
    @Column(name = "row_menu", nullable = false, columnDefinition = "tinyint(1)  default 0 comment '行菜单'")
    private Integer rowMenu;
    @Column(name = "column_filter", nullable = false, columnDefinition = "tinyint(1)  default 0 comment '列过滤器'")
    private Integer columnFilter;
    @Column(name = "status_command", nullable = false, columnDefinition = "tinyint(1)  default 0 comment '是否显示状态命令'")
    private Boolean statusCommand;
    @Column(name = "column_configs", columnDefinition = "text null comment '列配置'")
    private String columnConfigs;
    @Column(name = "source",updatable = false, nullable = false, columnDefinition = "int default 0 not  null comment '类来源'")
    private Integer source;
    @Column(name = "source_code", updatable = false, columnDefinition = "text null comment '源代码'")
    private String sourceCode;
}
