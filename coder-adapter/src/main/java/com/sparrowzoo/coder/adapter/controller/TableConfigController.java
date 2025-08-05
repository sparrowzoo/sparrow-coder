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
import com.sparrowzoo.coder.adapter.assemble.TableConfigAssemble;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.protocol.param.TableConfigParam;
import com.sparrowzoo.coder.domain.service.registry.ValidatorRegistry;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;
import com.sparrowzoo.coder.protocol.dto.TableConfigDTO;
import com.sparrowzoo.coder.domain.service.TableConfigService;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import com.sparrowzoo.coder.domain.service.ProjectConfigService;




@RestController
@RequestMapping("table/config")
@Api(value = "TableConfig",tags = "TableConfig")
public class TableConfigController {

    @Inject
    private TableConfigService tableConfigService;

    @Inject
    private TableConfigAssemble tableConfigAssemble;

    @Inject
private EnumsContainer coderEnumsContainer;
 @Inject
 private ProjectConfigService projectConfigService;
@Inject
private EnumsContainer businessEnumsContainer;

    @PostMapping("search.json")
    @ApiOperation("搜索")
    public PagerResult<TableConfigDTO> search(@RequestBody TableConfigQuery tableConfigQuery) {
        ListRecordTotalBO<TableConfigBO> tableConfigListTotalRecord = this.tableConfigService.queryTableConfig(tableConfigQuery);
        PagerResult<TableConfigDTO> pagerResult =this.tableConfigAssemble.assemblePager(tableConfigListTotalRecord, tableConfigQuery);
        pagerResult.putDictionary("status",coderEnumsContainer.getEnums("status"));
pagerResult.putDictionary("projectId",this.projectConfigService.getProjectConfigKvs());

pagerResult.putDictionary("source",businessEnumsContainer.getEnums("datasourceType"));
pagerResult.putDictionary("cellType",coderEnumsContainer.getEnums("cellType"));
pagerResult.putDictionary("datasourceType",coderEnumsContainer.getEnums("datasourceType"));
pagerResult.putDictionary("columnType",coderEnumsContainer.getEnums("columnType"));
pagerResult.putDictionary("controlType",coderEnumsContainer.getEnums("controlType"));
pagerResult.putDictionary("headerType",coderEnumsContainer.getEnums("headerType"));
pagerResult.putDictionary("searchType",coderEnumsContainer.getEnums("searchType"));
pagerResult.putDictionary("validateType", ValidatorRegistry.getInstance().getValidatorNames("react"));
        return pagerResult;
    }

    @PostMapping("save.json")
            @ApiOperation("保存")

    public Long saveTableConfig(@RequestBody TableConfigParam tableConfigParam) throws BusinessException {
       return  this.tableConfigService.saveTableConfig(tableConfigParam);
    }

    @GetMapping("detail.json")
            @ApiOperation("详情页")
    public TableConfigDTO getTableConfig(Long tableConfigId) throws BusinessException {
        TableConfigBO tableConfigBo = tableConfigService.getTableConfig(tableConfigId);
        return this.tableConfigAssemble.boAssembleDTO(tableConfigBo);
    }

    @PostMapping("delete.json")
            @ApiOperation("删除")

    public Integer deleteTableConfig(@RequestBody Set<Long> ids) throws BusinessException {
       return this.tableConfigService.deleteTableConfig(ids);
    }

    @PostMapping("enable.json")
            @ApiOperation("启用")

    public Integer enableTableConfig(@RequestBody Set<Long> ids) throws BusinessException {
        return  this.tableConfigService.enableTableConfig(ids);
    }

    @PostMapping("disable.json")
    @ApiOperation("禁用")
    public Integer disableTableConfig(@RequestBody Set<Long> ids) throws BusinessException {
       return  this.tableConfigService.disableTableConfig(ids);
    }
    }