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

import com.sparrow.protocol.DisplayTextAccessor;
import com.sparrow.protocol.dao.PO;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 注意implements DisplayTextAccessor 为自动提供列表搜索支持 必须添加
 */
@Table(name = "t_department")
@Data
public class Department extends PO implements DisplayTextAccessor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int COMMENT 'ID'")
    private Long id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(32) COMMENT '部门名称'")
    private String name;

    @Override
    public String getDisplayText() {
        return this.name;
    }
}
