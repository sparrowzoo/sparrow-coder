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
import com.sparrow.coder.adapter.assemble.UserExampleAssemble;
import com.sparrow.coder.domain.bo.UserExampleBO;
import com.sparrow.coder.domain.service.ProjectConfigService;
import com.sparrow.coder.domain.service.UserExampleService;
import com.sparrow.coder.protocol.dto.UserExampleDTO;
import com.sparrow.coder.protocol.param.UserExampleParam;
import com.sparrow.coder.protocol.query.UserExampleQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("user/example")
@Tag(name = "UserExample", description = "UserExample")
public class UserExampleController {

    @Inject
    private UserExampleService userExampleService;

    @Inject
    private UserExampleAssemble userExampleAssemble;

    @Inject
    private EnumsContainer coderEnumsContainer;
    @Inject
    private EnumsContainer businessEnumsContainer;
    @Inject
    private ProjectConfigService projectConfigService;

    @PostMapping("search.json")
    @Operation(method = "搜索")
    public PagerResult<UserExampleDTO> search(@RequestBody UserExampleQuery userExampleQuery) {
        ListRecordTotalBO<UserExampleBO> userExampleListTotalRecord = this.userExampleService.queryUserExample(userExampleQuery);
        PagerResult<UserExampleDTO> pagerResult = this.userExampleAssemble.assemblePager(userExampleListTotalRecord, userExampleQuery);
        pagerResult.putDictionary("status", coderEnumsContainer.getEnums("status"));
        pagerResult.putDictionary("gender", businessEnumsContainer.getEnums("gender"));
        pagerResult.putDictionary("projectId", this.projectConfigService.getProjectConfigKvs());

        return pagerResult;
    }

    @PostMapping("save.json")
    @Operation(method = "保存")

    public Long saveUserExample(@RequestBody UserExampleParam userExampleParam) throws BusinessException {
        return this.userExampleService.saveUserExample(userExampleParam);
    }

    @GetMapping("detail.json")
    @Operation(method = "详情页")
    public UserExampleDTO getUserExample(Long userExampleId) throws BusinessException {
        UserExampleBO userExampleBo = userExampleService.getUserExample(userExampleId);
        return this.userExampleAssemble.boAssembleDTO(userExampleBo);
    }

    @PostMapping("delete.json")
    @Operation(method = "删除")

    public Integer deleteUserExample(@RequestBody Set<Long> ids) throws BusinessException {
        return this.userExampleService.deleteUserExample(ids);
    }

    @PostMapping("enable.json")
    @Operation(method = "启用")

    public Integer enableUserExample(@RequestBody Set<Long> ids) throws BusinessException {
        return this.userExampleService.enableUserExample(ids);
    }

    @PostMapping("disable.json")
    @Operation(method = "禁用")
    public Integer disableUserExample(@RequestBody Set<Long> ids) throws BusinessException {
        return this.userExampleService.disableUserExample(ids);
    }
}
