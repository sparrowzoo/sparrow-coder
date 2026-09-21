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

import com.sparrow.protocol.*;

import java.util.*;

import com.sparrow.protocol.pager.PagerResult;
import com.sparrow.spring.container.EnumsContainer;
import com.sparrow.coder.adapter.assemble.TableConfigAssemble;
import com.sparrow.coder.domain.bo.TableConfigBO;
import com.sparrow.coder.protocol.param.TableConfigParam;
import com.sparrow.coder.domain.service.registry.ValidatorRegistry;
import com.sparrow.coder.protocol.query.TableConfigQuery;
import com.sparrow.coder.protocol.dto.TableConfigDTO;
import com.sparrow.coder.domain.service.TableConfigService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.*;
import com.sparrow.coder.domain.service.ProjectConfigService;

import com.sparrow.coder.utils.DefaultColumnsDefCreator;


@RestController
@RequestMapping("table/config")
@Tag(name = "TableConfig", description = "TableConfig")
public class TableConfigController {

    @Inject
    private TableConfigService tableConfigService;

    @Inject
    private TableConfigAssemble tableConfigAssemble;

    @Inject
    private EnumsContainer coderEnumsContainer;
    @Inject
    private ProjectConfigService projectConfigService;

    @PostMapping("search.json")
    @Operation(method = "搜索")
    public PagerResult<TableConfigDTO> search(@RequestBody TableConfigQuery tableConfigQuery) {
        ListRecordTotalBO<TableConfigBO> tableConfigListTotalRecord = this.tableConfigService.queryTableConfig(tableConfigQuery);
        PagerResult<TableConfigDTO> pagerResult = this.tableConfigAssemble.assemblePager(tableConfigListTotalRecord, tableConfigQuery);
        pagerResult.putDictionary("status", coderEnumsContainer.getEnums("status"));
        pagerResult.putDictionary("projectId", this.projectConfigService.getProjectConfigKvs());

        DefaultColumnsDefCreator.resetColumns(pagerResult.getList());

        pagerResult.putDictionary("cellType", coderEnumsContainer.getEnums("cellType"));
        pagerResult.putDictionary("datasourceType", coderEnumsContainer.getEnums("datasourceType"));
        pagerResult.putDictionary("columnType", coderEnumsContainer.getEnums("columnType"));
        pagerResult.putDictionary("controlType", coderEnumsContainer.getEnums("controlType"));
        pagerResult.putDictionary("headerType", coderEnumsContainer.getEnums("headerType"));
        pagerResult.putDictionary("searchType", coderEnumsContainer.getEnums("searchType"));
        pagerResult.putDictionary("source", coderEnumsContainer.getEnums("source"));
        pagerResult.putDictionary("validateType", ValidatorRegistry.getInstance().getValidatorNames("react"));
        return pagerResult;
    }

    @PostMapping("save.json")
    @Operation(method = "保存")

    public Long saveTableConfig(@RequestBody TableConfigParam tableConfigParam) throws BusinessException {
        return this.tableConfigService.saveTableConfig(tableConfigParam);
    }

    @GetMapping("detail.json")
    @Operation(method = "详情页")
    public TableConfigDTO getTableConfig(Long tableConfigId) throws BusinessException {
        TableConfigBO tableConfigBo = tableConfigService.getTableConfig(tableConfigId);
        return this.tableConfigAssemble.boAssembleDTO(tableConfigBo);
    }

    @PostMapping("delete.json")
    @Operation(method = "删除")

    public Integer deleteTableConfig(@RequestBody Set<Long> ids) throws BusinessException {
        return this.tableConfigService.deleteTableConfig(ids);
    }

    @PostMapping("enable.json")
    @Operation(method = "启用")

    public Integer enableTableConfig(@RequestBody Set<Long> ids) throws BusinessException {
        return this.tableConfigService.enableTableConfig(ids);
    }

    @PostMapping("disable.json")
    @Operation(method = "禁用")
    public Integer disableTableConfig(@RequestBody Set<Long> ids) throws BusinessException {
        return this.tableConfigService.disableTableConfig(ids);
    }
}
