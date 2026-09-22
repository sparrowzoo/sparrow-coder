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

package com.sparrow.coder.po;

import com.sparrow.protocol.DisplayTextAccessor;
import com.sparrow.protocol.dao.PO;
import jakarta.persistence.*;
import lombok.Data;

@Table(name = "t_project_config")
@Data
public class ProjectConfig extends PO implements DisplayTextAccessor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int comment 'ID'")
    private Long id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(50) default '' comment '项目名称'")
    private String name;
    @Column(name = "frontend_name",nullable = false,columnDefinition = "varchar(50) default '' comment '前端项目名称'")
    private String frontendName;
    @Column(name = "chinese_name",nullable = false,  columnDefinition = "varchar(50) default '' comment '项目中文名称'")
    private String chineseName;
    @Column(name = "i18n", columnDefinition = "tinyint(1) default 0 comment '是否支持国际化'")
    private Boolean i18n;
    @Column(name = "description", columnDefinition = "text default '' comment '项目描述'")
    private String description;
    @Column(name = "module_prefix", nullable  = false, columnDefinition = "varchar(50) default '' comment '模块前缀'")
    private String modulePrefix;
    @Column(name = "architectures", columnDefinition = "varchar(50) default '' comment '代码架构'")
    private String architectures;
    @Column(name = "config", columnDefinition = "varchar(512) default '' comment '脚手架配置'")
    private String config;
    @Column(name = "wrap_with_parent", columnDefinition = "tinyint(1) default 0 comment '是否使用父module'")
    private Boolean wrapWithParent;
    @Column(name = "scaffold", columnDefinition = "varchar(50) default '' comment '脚手架'")
    private String scaffold;

    @Override
    public String getDisplayText() {
        return String.format("%s【%s】", chineseName, name);
    }
}
