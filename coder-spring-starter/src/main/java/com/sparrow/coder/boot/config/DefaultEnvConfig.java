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

import java.io.File;

@Named
@Slf4j
public class DefaultEnvConfig implements EnvConfig {
    DefaultEnvConfig() {
        log.info("env config impl");
    }

    @Value("${workspace}")
    private String workspace;

    @Value("${project_root}")
    private String projectRoot;

    @Value("${front_project_root}")
    private String frontProjectRoot;

    @Value("${multi_user}")
    private Boolean multiUser;

    @Value("${overwrite}")
    private Boolean overwrite;


    @Override
    public String getWorkspace() {
        return this.workspace.replace("/", File.separator);
    }

    @Override
    public String getProjectRoot() {
        return this.projectRoot.replace("/", File.separator);
    }

    @Override
    public String getFrontProjectRoot() {
        return this.frontProjectRoot;
    }

    @Override
    public Boolean getMultiUser() {
        return this.multiUser;
    }

    @Override
    public String getHome(Long userId) {
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
