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

import com.sparrow.coder.domain.service.EnvConfig;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Named
@Slf4j
public class DefaultEnvConfig implements EnvConfig {
    DefaultEnvConfig() {
        log.info("env config impl");
    }

    @Value("${scaffold_workspace:}")
    private String workspace;

    @Value("${scaffold_backend_template_home:}")
    private String backendScaffoldTemplateHome;

    @Value("${scaffold_frontend_template_home:}")
    private String frontendScaffoldTemplateHome;


    @Value("${multi_user:false}")
    private Boolean multiUser;

    @Value("${overwrite:true}")
    private Boolean overwrite;


    @Override
    public String getScaffoldWorkspace() {
        return this.workspace;
    }

    @Override
    public String getBackendScaffoldTemplateHome() {
        return this.backendScaffoldTemplateHome;
    }

    @Override
    public String getFrontendScaffoldTemplateHome() {
        return this.frontendScaffoldTemplateHome;
    }
    @Override
    public Boolean getMultiUser() {
        return this.multiUser;
    }

    @Override
    public String getUserHome(Long userId) {
        if (this.multiUser) {
            return userId + "";
        }
        return "";
    }

    @Override
    public Boolean overwrite() {
        return this.overwrite;
    }
}
