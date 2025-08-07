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

import com.sparrow.protocol.*;
import java.util.*;
import com.sparrow.protocol.pager.PagerResult;
import com.sparrow.spring.starter.*;
import com.sparrowzoo.coder.adapter.assemble.UserExampleAssemble;
import com.sparrowzoo.coder.domain.bo.UserExampleBO;
import com.sparrowzoo.coder.protocol.param.UserExampleParam;
import com.sparrowzoo.coder.domain.service.registry.ValidatorRegistry;
import com.sparrowzoo.coder.protocol.query.UserExampleQuery;
import com.sparrowzoo.coder.protocol.dto.UserExampleDTO;
import com.sparrowzoo.coder.domain.service.UserExampleService;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;




@RestController
@RequestMapping("user/example")
@Api(value = "UserExample",tags = "UserExample")
public class UserExampleController {

    @Inject
    private UserExampleService userExampleService;

    @Inject
    private UserExampleAssemble userExampleAssemble;

    @Inject
private EnumsContainer coderEnumsContainer;
@Inject
private EnumsContainer businessEnumsContainer;

    @PostMapping("search.json")
    @ApiOperation("搜索")
    public PagerResult<UserExampleDTO> search(@RequestBody UserExampleQuery userExampleQuery) {
        ListRecordTotalBO<UserExampleBO> userExampleListTotalRecord = this.userExampleService.queryUserExample(userExampleQuery);
        PagerResult<UserExampleDTO> pagerResult =this.userExampleAssemble.assemblePager(userExampleListTotalRecord, userExampleQuery);
        pagerResult.putDictionary("status",coderEnumsContainer.getEnums("status"));
pagerResult.putDictionary("gender",businessEnumsContainer.getEnums("gender"));
        return pagerResult;
    }

    @PostMapping("save.json")
            @ApiOperation("保存")

    public Long saveUserExample(@RequestBody UserExampleParam userExampleParam) throws BusinessException {
       return  this.userExampleService.saveUserExample(userExampleParam);
    }

    @GetMapping("detail.json")
            @ApiOperation("详情页")
    public UserExampleDTO getUserExample(Long userExampleId) throws BusinessException {
        UserExampleBO userExampleBo = userExampleService.getUserExample(userExampleId);
        return this.userExampleAssemble.boAssembleDTO(userExampleBo);
    }

    @PostMapping("delete.json")
            @ApiOperation("删除")

    public Integer deleteUserExample(@RequestBody Set<Long> ids) throws BusinessException {
       return this.userExampleService.deleteUserExample(ids);
    }

    @PostMapping("enable.json")
            @ApiOperation("启用")

    public Integer enableUserExample(@RequestBody Set<Long> ids) throws BusinessException {
        return  this.userExampleService.enableUserExample(ids);
    }

    @PostMapping("disable.json")
    @ApiOperation("禁用")
    public Integer disableUserExample(@RequestBody Set<Long> ids) throws BusinessException {
       return  this.userExampleService.disableUserExample(ids);
    }
    }