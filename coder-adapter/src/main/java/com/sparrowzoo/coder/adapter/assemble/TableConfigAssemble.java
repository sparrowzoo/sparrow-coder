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
import com.sparrowzoo.coder.adapter.protocol.vo.TableConfigVO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.protocol.param.TableConfigParam;
import com.sparrow.support.assemble.BO2VOAssemble;
import com.sparrow.support.assemble.Param2VOAssemble;
import com.sparrow.utility.BeanUtility;
import com.sparrow.utility.CollectionsUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Named;

@Named
public class TableConfigAssemble implements BO2VOAssemble<TableConfigVO, TableConfigBO>,
    Param2VOAssemble<TableConfigVO,TableConfigParam> {

    public TableConfigVO paramAssembleVO(TableConfigParam param){
        TableConfigVO tableConfig = new TableConfigVO();
        BeanUtility.copyProperties(param, tableConfig);
        return tableConfig;
    }

    @Override public TableConfigVO boAssembleVO(TableConfigBO bo) {
        TableConfigVO tableConfig = new TableConfigVO();
        BeanUtility.copyProperties(bo, tableConfig);
        return tableConfig;
    }

    @Override public List<TableConfigVO> boListAssembleVOList(List<TableConfigBO> list) {
        if (CollectionsUtility.isNullOrEmpty(list)) {
            return Collections.emptyList();
        }
        List<TableConfigVO> tableConfigVOList = new ArrayList<>(list.size());
        for (TableConfigBO tableConfigBo : list) {
            tableConfigVOList.add(this.boAssembleVO(tableConfigBo));
        }
        return tableConfigVOList;
    }

    public PagerResult<TableConfigVO> assemblePager(ListRecordTotalBO<TableConfigBO> tableConfigListTotalRecord,
        SimplePager tableConfigQuery) {
        List<TableConfigVO> tableConfigVOList = this.boListAssembleVOList(tableConfigListTotalRecord.getList());
        PagerResult<TableConfigVO> pagerResult = new PagerResult<>(tableConfigQuery);
        pagerResult.setList(tableConfigVOList);
        pagerResult.setRecordTotal(tableConfigListTotalRecord.getTotal());
        return pagerResult;
    }
}