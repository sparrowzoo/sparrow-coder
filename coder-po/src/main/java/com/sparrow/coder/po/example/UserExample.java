/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.coder.po.example;

import com.sparrow.protocol.dao.ListDatasource;
import com.sparrow.protocol.dao.PO;
import com.sparrow.protocol.dao.enums.ListDatasourceType;
import jakarta.persistence.*;
import lombok.Data;

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

    @Column(name = "department_id", nullable = false, columnDefinition = "int comment '部门ID'")
    @ListDatasource(type = ListDatasourceType.TABLE, params = "t_department")
    private Long departmentId;
}
