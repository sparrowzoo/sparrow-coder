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

package com.sparrow.coder.adapter.controller;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ListRecordTotalBO;
import com.sparrow.protocol.pager.PagerResult;
import com.sparrow.spring.container.EnumsContainer;
import com.sparrow.coder.adapter.assemble.ProjectConfigAssemble;
import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.domain.service.ProjectConfigService;
import com.sparrow.coder.protocol.dto.ProjectConfigDTO;
import com.sparrow.coder.protocol.param.ProjectConfigParam;
import com.sparrow.coder.protocol.query.ProjectConfigQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("project/config")
@Tag(name = "ProjectConfig", description = "ProjectConfig")
public class ProjectConfigController {

    @Inject
    private ProjectConfigService projectConfigService;

    @Inject
    private ProjectConfigAssemble projectConfigAssemble;

    @Inject
    private EnumsContainer coderEnumsContainer;

    @PostMapping("search.json")
    @Operation(method = "搜索")
    public PagerResult<ProjectConfigDTO> search(@RequestBody ProjectConfigQuery projectConfigQuery) {
        ListRecordTotalBO<ProjectConfigBO> projectConfigListTotalRecord = this.projectConfigService.queryProjectConfig(projectConfigQuery);
        PagerResult<ProjectConfigDTO> pagerResult = this.projectConfigAssemble.assemblePager(projectConfigListTotalRecord, projectConfigQuery);
        pagerResult.putDictionary("status", coderEnumsContainer.getEnums("status"));
        return pagerResult;
    }

    @PostMapping("save.json")
    @Operation(method = "保存")

    public Long saveProjectConfig(@RequestBody ProjectConfigParam projectConfigParam) throws BusinessException {
        return this.projectConfigService.saveProjectConfig(projectConfigParam);
    }

    @GetMapping("detail.json")
    @Operation(method = "详情页")
    public ProjectConfigDTO getProjectConfig(Long projectConfigId) throws BusinessException {
        ProjectConfigBO projectConfigBo = projectConfigService.getProjectConfig(projectConfigId);
        return this.projectConfigAssemble.boAssembleDTO(projectConfigBo);
    }

    @PostMapping("delete.json")
    @Operation(method = "删除")

    public Integer deleteProjectConfig(@RequestBody Set<Long> ids) throws BusinessException {
        return this.projectConfigService.deleteProjectConfig(ids);
    }

    @PostMapping("enable.json")
    @Operation(method = "启用")

    public Integer enableProjectConfig(@RequestBody Set<Long> ids) throws BusinessException {
        return this.projectConfigService.enableProjectConfig(ids);
    }

    @PostMapping("disable.json")
    @Operation(method = "禁用")
    public Integer disableProjectConfig(@RequestBody Set<Long> ids) throws BusinessException {
        return this.projectConfigService.disableProjectConfig(ids);
    }
}
