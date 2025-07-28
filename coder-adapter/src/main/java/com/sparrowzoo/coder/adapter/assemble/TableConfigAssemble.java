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
import com.sparrowzoo.coder.protocol.dto.TableConfigDTO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrow.utility.CollectionsUtility;
import java.util.*;
import javax.inject.*;
import com.sparrow.protocol.BeanCopier;


@Named
public class TableConfigAssemble{

    @Inject
    private BeanCopier beanCopier;

     public TableConfigDTO boAssembleDTO(TableConfigBO bo) {
        TableConfigDTO tableConfig = new TableConfigDTO();
        beanCopier.copyProperties(bo, tableConfig);
        return tableConfig;
    }

     public List<TableConfigDTO> boListAssembleDTOList(List<TableConfigBO> list) {
        if (CollectionsUtility.isNullOrEmpty(list)) {
            return Collections.emptyList();
        }
        List<TableConfigDTO> tableConfigDTOList = new ArrayList<>(list.size());
        for (TableConfigBO tableConfigBo : list) {
            tableConfigDTOList.add(this.boAssembleDTO(tableConfigBo));
        }
        return tableConfigDTOList;
    }

    public PagerResult<TableConfigDTO> assemblePager(ListRecordTotalBO<TableConfigBO> tableConfigListTotalRecord,
        SimplePager tableConfigQuery) {
        List<TableConfigDTO> tableConfigDTOList = this.boListAssembleDTOList(tableConfigListTotalRecord.getList());
        PagerResult<TableConfigDTO> pagerResult = new PagerResult<>(tableConfigQuery);
        pagerResult.setList(tableConfigDTOList);
        pagerResult.setRecordTotal(tableConfigListTotalRecord.getTotal());
        return pagerResult;
    }

}