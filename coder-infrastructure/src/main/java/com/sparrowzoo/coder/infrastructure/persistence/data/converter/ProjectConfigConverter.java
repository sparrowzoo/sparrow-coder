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
package com.sparrowzoo.coder.infrastructure.persistence.data.converter;

import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.ThreadContext;
import com.sparrow.protocol.dao.StatusCriteria;
import com.sparrow.support.converter.POInitUtils;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.po.ProjectConfig;
import com.sparrowzoo.coder.protocol.param.ProjectConfigParam;
import com.sparrowzoo.coder.protocol.query.ProjectConfigQuery;
import com.sparrow.support.converter.PO2BOConverter;
import com.sparrow.support.converter.Param2POConverter;
import com.sparrowzoo.coder.dao.query.ProjectConfigDBPagerQuery;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Inject;
import com.sparrow.protocol.BeanCopier;


@Named
public class ProjectConfigConverter implements Param2POConverter<ProjectConfigParam, ProjectConfig>, PO2BOConverter<ProjectConfigBO, ProjectConfig> {

    @Inject
    private BeanCopier beanCopier;

    public ProjectConfigDBPagerQuery toDbPagerQuery(ProjectConfigQuery projectConfigQuery) {
           if (projectConfigQuery == null) {
               return new ProjectConfigDBPagerQuery();
           }
           ProjectConfigDBPagerQuery projectConfig = new ProjectConfigDBPagerQuery();
           beanCopier.copyProperties(projectConfigQuery, projectConfig);
           return projectConfig;
       }

    @Override public ProjectConfig param2po(ProjectConfigParam param) {
        ProjectConfig projectConfig = new ProjectConfig();
        beanCopier.copyProperties(param, projectConfig);
        POInitUtils.init(projectConfig);

        return projectConfig;
    }

    @Override public ProjectConfigBO po2bo(ProjectConfig projectConfig) {
        ProjectConfigBO projectConfigBO = new ProjectConfigBO();
        beanCopier.copyProperties(projectConfig, projectConfigBO);
        return projectConfigBO;
    }

    @Override public List<ProjectConfigBO> poList2BoList(List<ProjectConfig> list) {
        List<ProjectConfigBO> projectConfigBos = new ArrayList<>(list.size());
        for (ProjectConfig projectConfig : list) {
            projectConfigBos.add(this.po2bo(projectConfig));
        }
        return projectConfigBos;
    }

    public void convertStatus(StatusCriteria statusCriteria){
            LoginUser loginUser = ThreadContext.getLoginToken();
            statusCriteria.setModifiedUserName(loginUser.getUserName());
            statusCriteria.setGmtModified(System.currentTimeMillis());
            statusCriteria.setModifiedUserId(loginUser.getUserId());
    }
}