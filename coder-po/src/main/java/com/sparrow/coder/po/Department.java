package com.sparrow.coder.po;

import com.sparrow.protocol.dao.PO;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "t_department")
@Data
public class Department extends PO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int COMMENT 'ID'")
    private Long id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(32) COMMENT '用户名'")
    private String userName;
}
