package com.sparrowzoo.coder.po;

import com.sparrow.protocol.dao.ListDatasource;
import com.sparrow.protocol.dao.PO;
import com.sparrow.protocol.dao.enums.ListDatasourceType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Table(name = "t_user_example")
public class UserExample extends PO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int COMMENT 'ID'")
    private Long id;
    @Column(name = "user_name", nullable = false, columnDefinition = "varchar(32) COMMENT '用户名'")
    private String userName;

    @Column(name = "chinese_name", nullable = false, columnDefinition = "varchar(32)  default '' comment '中文名'")
    private String chineseName;
    @Column(name = "birthday", nullable = false, columnDefinition = "date null comment '出生日期'")
    private LocalDate birthday;
    @Column(name = "email", nullable = false, unique = true, columnDefinition = "varchar(128)  default '' comment 'Email'")
    private String email;

    @Column(name = "mobile", nullable = false, unique = true, columnDefinition = "varchar(128)  default '' comment '手机号'")
    private String mobile;

    @Column(name = "tel", nullable = false, unique = true, columnDefinition = "varchar(128)  default '' comment '电话号码'")
    private String tel;

    @Column(name = "id_card", nullable = false, unique = true, columnDefinition = "varchar(128)  default '' comment '身份证'")
    private String idCard;

    @Column(name = "gender", nullable = false, unique = true, columnDefinition = "int not null default 999 comment '性别'")
    @ListDatasource(type = ListDatasourceType.ENUM, params = "gender")
    private Integer gender;

    @Column(name = "age", nullable = false, unique = true, columnDefinition = "int not null default 0 comment '年龄'")
    private Integer age;
}
