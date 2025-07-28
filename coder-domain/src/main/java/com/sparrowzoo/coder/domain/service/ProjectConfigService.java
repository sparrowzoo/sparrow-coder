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

package com.sparrowzoo.coder.domain.service;

import com.sparrow.exception.Asserts;
import com.sparrow.protocol.*;
import java.util.*;
import javax.inject.*;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.repository.ProjectConfigRepository;
import com.sparrowzoo.coder.protocol.param.ProjectConfigParam;
import com.sparrowzoo.coder.protocol.query.ProjectConfigQuery;
import com.sparrow.utility.CollectionsUtility;


@Named
public class ProjectConfigService {
    @Inject
    private ProjectConfigRepository projectConfigRepository;

    private void validateSaveProjectConfig(ProjectConfigParam projectConfigParam) throws BusinessException {
        //Asserts.isTrue(StringUtility.isNullOrEmpty(projectConfigParam.getName()), SecurityAdminError.NAME_IS_EMPTY, ProjectConfigSuffix.name);
    }

    public Long saveProjectConfig(ProjectConfigParam projectConfigParam) throws BusinessException {
        this.validateSaveProjectConfig(projectConfigParam);
        return this.projectConfigRepository.save(projectConfigParam);
    }

    public Integer deleteProjectConfig(Set<Long> projectConfigIds) throws BusinessException {
        Asserts.isTrue(CollectionsUtility.isNullOrEmpty(projectConfigIds), SparrowError.GLOBAL_PARAMETER_NULL);
        return this.projectConfigRepository.delete(projectConfigIds);
    }

    public Integer enableProjectConfig(Set<Long> projectConfigIds) throws BusinessException {
        Asserts.isTrue(CollectionsUtility.isNullOrEmpty(projectConfigIds), SparrowError.GLOBAL_PARAMETER_NULL);
        return this.projectConfigRepository.enable(projectConfigIds);
    }

    public Integer disableProjectConfig(Set<Long> projectConfigIds) throws BusinessException {
        Asserts.isTrue(CollectionsUtility.isNullOrEmpty(projectConfigIds), SparrowError.GLOBAL_PARAMETER_NULL);
        return this.projectConfigRepository.disable(projectConfigIds);
    }

    public ListRecordTotalBO<ProjectConfigBO> queryAllProjectConfig() {
        return queryProjectConfig(null);
    }

    public ListRecordTotalBO<ProjectConfigBO> queryProjectConfig(ProjectConfigQuery projectConfigQuery) {
        Long totalRecord = this.projectConfigRepository.getProjectConfigCount(projectConfigQuery);
        List<ProjectConfigBO> projectConfigBoList = null;
        if (totalRecord > 0) {
            projectConfigBoList = this.projectConfigRepository.queryProjectConfigs(projectConfigQuery);
        }
        return new ListRecordTotalBO<>(projectConfigBoList, totalRecord);
    }

    public ProjectConfigBO getProjectConfig(Long projectConfigId) throws BusinessException {
         Asserts.isTrue(projectConfigId==null, SparrowError.GLOBAL_PARAMETER_NULL);
        return this.projectConfigRepository.getProjectConfig(projectConfigId);
    }

    public List<KeyValue<Integer, String>> getProjectConfigKvs() {
        ProjectConfigQuery projectConfigQuery = new ProjectConfigQuery();
        projectConfigQuery.setStatus(StatusRecord.ENABLE.ordinal());
        projectConfigQuery.setPageSize(-1);
        List<ProjectConfigBO> projectConfigBoList = this.projectConfigRepository.queryProjectConfigs(projectConfigQuery);
        List<KeyValue<Integer, String>> projectConfigKvs = new ArrayList<>(projectConfigBoList.size());
        for (ProjectConfigBO projectConfigBO : projectConfigBoList) {
            projectConfigKvs.add(new KeyValue<>(projectConfigBO.getId().intValue(), projectConfigBO.getDisplayText()));
        }
        return projectConfigKvs;
    }
}