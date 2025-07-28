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

package com.sparrowzoo.coder.adapter.assemble;

import com.sparrow.protocol.ListRecordTotalBO;
import com.sparrow.protocol.KeyValue;
import com.sparrow.protocol.pager.PagerResult;
import com.sparrow.protocol.pager.SimplePager;
import com.sparrowzoo.coder.protocol.dto.ProjectConfigDTO;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrow.utility.CollectionsUtility;
import java.util.*;
import javax.inject.*;
import com.sparrow.protocol.BeanCopier;


@Named
public class ProjectConfigAssemble{

    @Inject
    private BeanCopier beanCopier;

     public ProjectConfigDTO boAssembleDTO(ProjectConfigBO bo) {
        ProjectConfigDTO projectConfig = new ProjectConfigDTO();
        beanCopier.copyProperties(bo, projectConfig);
        return projectConfig;
    }

     public List<ProjectConfigDTO> boListAssembleDTOList(List<ProjectConfigBO> list) {
        if (CollectionsUtility.isNullOrEmpty(list)) {
            return Collections.emptyList();
        }
        List<ProjectConfigDTO> projectConfigDTOList = new ArrayList<>(list.size());
        for (ProjectConfigBO projectConfigBo : list) {
            projectConfigDTOList.add(this.boAssembleDTO(projectConfigBo));
        }
        return projectConfigDTOList;
    }

    public PagerResult<ProjectConfigDTO> assemblePager(ListRecordTotalBO<ProjectConfigBO> projectConfigListTotalRecord,
        SimplePager projectConfigQuery) {
        List<ProjectConfigDTO> projectConfigDTOList = this.boListAssembleDTOList(projectConfigListTotalRecord.getList());
        PagerResult<ProjectConfigDTO> pagerResult = new PagerResult<>(projectConfigQuery);
        pagerResult.setList(projectConfigDTOList);
        pagerResult.setRecordTotal(projectConfigListTotalRecord.getTotal());
        return pagerResult;
    }

}