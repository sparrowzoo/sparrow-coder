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

package com.sparrow.coder.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Passport starter 的 OpenAPI 元信息配置。
 *
 * <p>统一使用 {@code sparrow.passport.openapi.*} 前缀，避免与 springdoc 官方
 * {@code springdoc.*} 以及宿主应用的配置命名空间冲突，从而实现 starter 与宿主项目的隔离。
 */
@ConfigurationProperties(prefix = "sparrow.openapi.coder.group")
@Data
public class CoderOpenApiProperties {

    /**
     * Swagger UI 分组名称，用于右上角下拉中隔离不同应用的接口文档。
     */
    private String group = "coder";

    /**
     * 该分组要扫描的包，默认只扫描本 starter 的包，实现接口文档隔离。
     */
    private String packagesToScan = "com.sparrow.coder";
}
