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

import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(OpenAPI.class)
@ComponentScan("com.sparrow.coder")
@EnableConfigurationProperties(CoderOpenApiProperties.class)
@Slf4j
public class CoderAutoConfiguration {

    public CoderAutoConfiguration() {
        log.info("CoderAutoConfiguration init");
    }

    /**
     * 注册一个分组，用于在 Swagger UI 右上角下拉中按应用隔离接口文档。
     *
     * <ul>
     *   <li>{@link ConditionalOnMissingBean}（按 bean 名）：仅当宿主没有同名 {@code passportGroup}
     *       时才注册，避免覆盖宿主自定义，同时允许宿主再注册其它分组共存。</li>
     *   <li>{@link ConditionalOnProperty}：与 {@code sparrow.passport.openapi.enabled} 联动，
     *       关闭时一并移除分组。</li>
     * </ul>
     */
    @Bean
    @ConditionalOnMissingBean(name = "coderGroup")
    public GroupedOpenApi coderGroup(CoderOpenApiProperties properties) {
        return GroupedOpenApi.builder()
                .group(properties.getGroup())
                .packagesToScan(properties.getPackagesToScan())
                .build();
    }
}
