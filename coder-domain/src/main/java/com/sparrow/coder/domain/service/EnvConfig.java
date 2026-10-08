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

package com.sparrow.coder.domain.service;

import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.StringUtility;

public interface EnvConfig {

    /**
     * 生成后脚手架的工作目录
     *
     * @return
     */
    public String getScaffoldWorkspace();

    /**
     * 后端脚手架地址
     *
     * @return
     */
    public String getBackendScaffoldTemplateHome();

    /**
     * 前端脚手架地址
     *
     * @return
     */
    public String getFrontendScaffoldTemplateHome();

    /**
     * 指定的前端项目地址 不指定则在当前项目中
     * 脚手架生成时 会在当前项目中生成前端代码
     * 实际生成代码时，会读该配置，用户可能会迁移前端目录
     * @return
     */
    public String getScaffoldFrontendHome();

    public Boolean getMultiUser();

    /**
     * 用户的家目录 相对于脚手架workspace
     *
     * @param userId
     * @return
     */
    public String getUserHome(Long userId);

    public Boolean overwrite();

    public default String getProjectHome(ProjectConfigBO projectConfig) {
        if (StringUtility.isNullOrEmpty(this.getScaffoldWorkspace())) {
            return EnvironmentSupport.getInstance().getWorkspace(projectConfig.getName());
        }
        String userHome = this.getUserHome(projectConfig.getCreateUserId());
        return new FileNameBuilder(this.getScaffoldWorkspace()).joint(userHome).joint(projectConfig.getName()).build();
    }

}
