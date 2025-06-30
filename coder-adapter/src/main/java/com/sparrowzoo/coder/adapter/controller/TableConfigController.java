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
import com.sparrowzoo.coder.adapter.assemble.TableConfigAssemble;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.protocol.param.TableConfigParam;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;
import com.sparrowzoo.coder.adapter.protocol.vo.TableConfigVO;
import com.sparrowzoo.coder.domain.service.TableConfigService;
import javax.inject.Inject;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Set;


@RestController
@RequestMapping("table/config")
@Api(value = "TableConfig",tags = "TableConfig")
public class TableConfigController {

    @Inject
    private TableConfigService tableConfigService;

    @Inject
    private TableConfigAssemble tableConfigAssemble;

    @PostMapping("search.json")
    @ApiOperation("搜索")
    public PagerResult<TableConfigVO> search(@RequestBody TableConfigQuery tableConfigQuery) {
        ListRecordTotalBO<TableConfigBO> tableConfigListTotalRecord = this.tableConfigService.queryTableConfig(tableConfigQuery);
        return this.tableConfigAssemble.assemblePager(tableConfigListTotalRecord, tableConfigQuery);
    }

    @PostMapping("save.json")
            @ApiOperation("保存")

    public Long saveTableConfig(@RequestBody TableConfigParam tableConfigParam) throws BusinessException {
       return  this.tableConfigService.saveTableConfig(tableConfigParam);
    }

    @GetMapping("detail.json")
            @ApiOperation("详情页")
    public TableConfigVO getTableConfig(Long tableConfigId) throws BusinessException {
        TableConfigBO tableConfigBo = tableConfigService.getTableConfig(tableConfigId);
        return this.tableConfigAssemble.boAssembleVO(tableConfigBo);
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