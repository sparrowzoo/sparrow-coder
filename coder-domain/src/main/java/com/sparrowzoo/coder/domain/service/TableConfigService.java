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
package com.sparrowzoo.coder.domain.service;

import com.sparrow.exception.Asserts;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ListRecordTotalBO;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.repository.TableConfigRepository;
import com.sparrowzoo.coder.protocol.param.TableConfigParam;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;
import com.sparrow.utility.CollectionsUtility;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class TableConfigService {
    @Inject
    private TableConfigRepository tableConfigRepository;

    private void validateSaveTableConfig(TableConfigParam tableConfigParam) throws BusinessException {
        //Asserts.isTrue(StringUtility.isNullOrEmpty(tableConfigParam.getName()), SecurityAdminError.NAME_IS_EMPTY, TableConfigSuffix.name);
    }

    public Long saveTableConfig(TableConfigParam tableConfigParam) throws BusinessException {
        this.validateSaveTableConfig(tableConfigParam);
        return this.tableConfigRepository.save(tableConfigParam);
    }

    public Integer deleteTableConfig(Set<Long> tableConfigIds) throws BusinessException {
        Asserts.isTrue(CollectionsUtility.isNullOrEmpty(tableConfigIds), SparrowError.GLOBAL_PARAMETER_NULL);
        return this.tableConfigRepository.delete(tableConfigIds);
    }

    public Integer enableTableConfig(Set<Long> tableConfigIds) throws BusinessException {
        Asserts.isTrue(CollectionsUtility.isNullOrEmpty(tableConfigIds), SparrowError.GLOBAL_PARAMETER_NULL);
        return this.tableConfigRepository.enable(tableConfigIds);
    }

    public Integer disableTableConfig(Set<Long> tableConfigIds) throws BusinessException {
        Asserts.isTrue(CollectionsUtility.isNullOrEmpty(tableConfigIds), SparrowError.GLOBAL_PARAMETER_NULL);
        return this.tableConfigRepository.disable(tableConfigIds);
    }

    public ListRecordTotalBO<TableConfigBO> queryAllTableConfig() {
        return queryTableConfig(null);
    }

    public ListRecordTotalBO<TableConfigBO> queryTableConfig(TableConfigQuery tableConfigQuery) {
        Long totalRecord = this.tableConfigRepository.getTableConfigCount(tableConfigQuery);
        List<TableConfigBO> tableConfigBoList = null;
        if (totalRecord > 0) {
            tableConfigBoList = this.tableConfigRepository.queryTableConfigs(tableConfigQuery);
        }
        return new ListRecordTotalBO<>(tableConfigBoList, totalRecord);
    }

    public TableConfigBO getTableConfig(Long tableConfigId) throws BusinessException {
         Asserts.isTrue(tableConfigId==null, SparrowError.GLOBAL_PARAMETER_NULL);
        return this.tableConfigRepository.getTableConfig(tableConfigId);
    }
}