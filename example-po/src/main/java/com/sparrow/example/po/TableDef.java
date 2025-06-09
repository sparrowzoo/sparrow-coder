package com.sparrow.example.po;

import com.sparrow.protocol.FieldOrder;
import com.sparrow.protocol.dao.PO;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "t_table_config")
public class TableDef extends PO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int")
    @FieldOrder(order = 0)
    private Long id;
    @Column(name = "table_name", updatable = false, nullable = false, unique = true, columnDefinition = "varchar(32)  default '' comment '表名'")
    private String tableName;
    @Column(name = "class_name", updatable = false, nullable = false, unique = true, columnDefinition = "varchar(32)  default '' comment '类名'")
    private String className;
    @Column(name = "description", nullable = false, columnDefinition = "varchar(255)  default '' comment '描述'")
    private String description;
    @Column(name = "checkable", nullable = false, columnDefinition = "tinyint(1)  default 0 comment '是否可勾选 0-不可 1-可'")
    private Boolean checkable;
    @Column(name = "row_menu", nullable = false, columnDefinition = "tinyint(1)  default 0 comment '是否显示行操作 0-不显示 1-显示'")
    private Boolean rowMenu;
    @Column(name = "column_filter", nullable = false, columnDefinition = "tinyint(1)  default 0 comment '是否显示列过滤器 0-不显示 1-显示'")
    private Boolean columnFilter;
    @Column(name = "status_command", nullable = false, columnDefinition = "tinyint(1)  default 0 comment '是否显示状态命令 0-不显示 1-显示'")
    private Boolean statusCommand;
    @Column(name = "column_configs", columnDefinition = "text  default '' comment '列配置'")
    private String columnConfigs;
}
