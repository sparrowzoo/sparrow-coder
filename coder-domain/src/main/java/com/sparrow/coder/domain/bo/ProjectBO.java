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

package com.sparrow.coder.domain.bo;

import com.sparrow.coder.domain.service.ArchitectureGenerator;
import com.sparrow.coder.domain.service.EnvConfig;
import com.sparrow.coder.enums.ArchitectureCategory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Data
public class ProjectBO {

    public ProjectBO(ProjectConfigBO projectConfig, Properties scaffoldConfig, EnvConfig envConfig) {
        this.projectConfig = projectConfig;
        this.scaffoldConfig = scaffoldConfig;
        this.envConfig = envConfig;
        this.architectures = new ProjectArchsBO(this.projectConfig.getArchitectures());
    }

    private ProjectConfigBO projectConfig;
    private Properties scaffoldConfig;
    private EnvConfig envConfig;
    private List<String> i18nList = new ArrayList<>();
    private ProjectArchsBO architectures;

    public ArchitectureGenerator getArchitecture(ArchitectureCategory architectureCategory) {
        return this.architectures.getArch(architectureCategory);
    }

    public void addI18n(String i18n) {
        this.i18nList.add(i18n);
    }
}
