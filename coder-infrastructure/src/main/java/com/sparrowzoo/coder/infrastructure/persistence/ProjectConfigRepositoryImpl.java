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
package com.sparrowzoo.coder.infrastructure.persistence;

import com.sparrow.protocol.dao.StatusCriteria;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrowzoo.coder.dao.ProjectConfigDAO;
import com.sparrowzoo.coder.infrastructure.persistence.data.converter.ProjectConfigConverter;
import com.sparrowzoo.coder.po.ProjectConfig;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.protocol.param.ProjectConfigParam;
import com.sparrowzoo.coder.repository.ProjectConfigRepository;
import com.sparrowzoo.coder.protocol.query.ProjectConfigQuery;

import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ProjectConfigRepositoryImpl implements ProjectConfigRepository {
    @Inject
    private ProjectConfigConverter projectConfigConverter;

    @Inject
    private ProjectConfigDAO projectConfigDao;

    @Override public Long save(ProjectConfigParam projectConfigParam) {
        ProjectConfig projectConfig = this.projectConfigConverter.param2po(projectConfigParam);
        if (projectConfig.getId() != null) {
            this.projectConfigDao.update(projectConfig);
            return projectConfig.getId();
        }
        this.projectConfigDao.insert(projectConfig);
        return projectConfig.getId();
    }

    @Override public Integer delete(Set<Long> projectConfigIds) {
        return this.projectConfigDao.batchDelete(projectConfigIds);
    }

    @Override public Integer disable(Set<Long> projectConfigIds) {
        StatusCriteria<Long> statusCriteria = new StatusCriteria(projectConfigIds, StatusRecord.DISABLE);
        this.projectConfigConverter.convertStatus(statusCriteria);
        return this.projectConfigDao.changeStatus(statusCriteria);
    }

    @Override public Integer enable(Set<Long> projectConfigIds) {
        StatusCriteria<Long> statusCriteria = new StatusCriteria(projectConfigIds, StatusRecord.ENABLE);
        this.projectConfigConverter.convertStatus(statusCriteria);
        return this.projectConfigDao.changeStatus(statusCriteria);
    }

    @Override public ProjectConfigBO getProjectConfig(Long projectConfigId) {
        ProjectConfig projectConfig = this.projectConfigDao.getEntity(projectConfigId);
        return this.projectConfigConverter.po2bo(projectConfig);
    }

    @Override public List<ProjectConfigBO> queryProjectConfigs(ProjectConfigQuery projectConfigQuery) {
        List<ProjectConfig> projectConfigList = this.projectConfigDao.queryProjectConfigs(this.projectConfigConverter.toDbPagerQuery(projectConfigQuery));
        return this.projectConfigConverter.poList2BoList(projectConfigList);
    }

    @Override public Long getProjectConfigCount(ProjectConfigQuery projectConfigQuery) {
        return this.projectConfigDao.countProjectConfig(this.projectConfigConverter.toDbPagerQuery(projectConfigQuery));
    }
}