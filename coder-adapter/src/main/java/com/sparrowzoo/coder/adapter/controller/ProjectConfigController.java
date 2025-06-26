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
package com.sparrowzoo.coder.adapter.controller;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ListRecordTotalBO;
import com.sparrow.protocol.pager.PagerResult;
import com.sparrowzoo.coder.adapter.assemble.ProjectConfigAssemble;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.protocol.param.ProjectConfigParam;
import com.sparrowzoo.coder.protocol.query.ProjectConfigQuery;
import com.sparrowzoo.coder.adapter.protocol.vo.ProjectConfigVO;
import com.sparrowzoo.coder.domain.service.ProjectConfigService;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("project/config")
@Api(value = "ProjectConfig",tags = "ProjectConfig")
public class ProjectConfigController {

    @Inject
    private ProjectConfigService projectConfigService;

    @Inject
    private ProjectConfigAssemble projectConfigAssemble;

    @PostMapping("search.json")
    @ApiOperation("搜索")
    public PagerResult<ProjectConfigVO> search(@RequestBody ProjectConfigQuery projectConfigQuery) {
        ListRecordTotalBO<ProjectConfigBO> projectConfigListTotalRecord = this.projectConfigService.queryProjectConfig(projectConfigQuery);
        return this.projectConfigAssemble.assemblePager(projectConfigListTotalRecord, projectConfigQuery);
    }

    @PostMapping("save.json")
            @ApiOperation("保存")

    public Long saveProjectConfig(@RequestBody ProjectConfigParam projectConfigParam) throws BusinessException {
       return  this.projectConfigService.saveProjectConfig(projectConfigParam);
    }

    @GetMapping("detail.json")
            @ApiOperation("详情页")
    public ProjectConfigVO getProjectConfig( Long projectConfigId) throws BusinessException {
        ProjectConfigBO projectConfigBo = projectConfigService.getProjectConfig(projectConfigId);
        return this.projectConfigAssemble.boAssembleVO(projectConfigBo);
    }

    @PostMapping("delete.json")
            @ApiOperation("删除")

    public Integer deleteProjectConfig(@RequestBody String ids) throws BusinessException {
       return this.projectConfigService.deleteProjectConfig(ids);
    }

    @PostMapping("enable.json")
            @ApiOperation("启用")

    public Integer enableProjectConfig(@RequestBody String ids) throws BusinessException {
        return  this.projectConfigService.enableProjectConfig(ids);
    }

    @PostMapping("disable.json")
    @ApiOperation("禁用")
    public Integer disableProjectConfig(@RequestBody String ids) throws BusinessException {
       return  this.projectConfigService.disableProjectConfig(ids);
    }
}