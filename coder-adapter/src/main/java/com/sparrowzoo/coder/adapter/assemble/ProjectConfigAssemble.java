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
import com.sparrow.protocol.pager.PagerResult;
import com.sparrow.protocol.pager.SimplePager;
import com.sparrowzoo.coder.adapter.protocol.vo.ProjectConfigVO;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.protocol.param.ProjectConfigParam;
import com.sparrow.support.assemble.BO2VOAssemble;
import com.sparrow.support.assemble.Param2VOAssemble;
import com.sparrow.utility.BeanUtility;
import com.sparrow.utility.CollectionsUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Named;

@Named
public class ProjectConfigAssemble implements BO2VOAssemble<ProjectConfigVO, ProjectConfigBO>,
    Param2VOAssemble<ProjectConfigVO,ProjectConfigParam> {

    public ProjectConfigVO paramAssembleVO(ProjectConfigParam param){
        ProjectConfigVO projectConfig = new ProjectConfigVO();
        BeanUtility.copyProperties(param, projectConfig);
        return projectConfig;
    }

    @Override public ProjectConfigVO boAssembleVO(ProjectConfigBO bo) {
        ProjectConfigVO projectConfig = new ProjectConfigVO();
        BeanUtility.copyProperties(bo, projectConfig);
        return projectConfig;
    }



    @Override public List<ProjectConfigVO> boListAssembleVOList(List<ProjectConfigBO> list) {
        if (CollectionsUtility.isNullOrEmpty(list)) {
            return Collections.emptyList();
        }
        List<ProjectConfigVO> projectConfigVOList = new ArrayList<>(list.size());
        for (ProjectConfigBO projectConfigBo : list) {
            projectConfigVOList.add(this.boAssembleVO(projectConfigBo));
        }
        return projectConfigVOList;
    }

    public PagerResult<ProjectConfigVO> assemblePager(ListRecordTotalBO<ProjectConfigBO> projectConfigListTotalRecord,
        SimplePager projectConfigQuery) {
        List<ProjectConfigVO> projectConfigVOList = this.boListAssembleVOList(projectConfigListTotalRecord.getList());
        PagerResult<ProjectConfigVO> pagerResult = new PagerResult<>(projectConfigQuery);
        pagerResult.setList(projectConfigVOList);
        pagerResult.setRecordTotal(projectConfigListTotalRecord.getTotal());
        return pagerResult;
    }
}
