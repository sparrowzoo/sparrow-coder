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
package com.sparrowzoo.coder.infrastructure.persistence;

import com.sparrow.protocol.dao.StatusCriteria;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrowzoo.coder.dao.TableConfigDAO;
import com.sparrowzoo.coder.infrastructure.persistence.data.converter.TableConfigConverter;
import com.sparrowzoo.coder.po.TableConfig;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.protocol.param.TableConfigParam;
import com.sparrowzoo.coder.repository.TableConfigRepository;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class TableConfigRepositoryImpl implements TableConfigRepository {
    @Inject
    private TableConfigConverter tableConfigConverter;

    @Inject
    private TableConfigDAO tableConfigDao;

    @Override public Long save(TableConfigParam tableConfigParam) {
        TableConfig tableConfig = this.tableConfigConverter.param2po(tableConfigParam);
        if (tableConfig.getId() != null) {
            this.tableConfigDao.update(tableConfig);
            return tableConfig.getId();
        }
        this.tableConfigDao.insert(tableConfig);
        return tableConfig.getId();
    }

    @Override public Integer delete(String tableConfigIds) {
        return this.tableConfigDao.batchDelete(tableConfigIds);
    }

    @Override public Integer disable(String tableConfigIds) {
        StatusCriteria statusCriteria = new StatusCriteria(tableConfigIds, StatusRecord.DISABLE);
        this.tableConfigConverter.convertStatus(statusCriteria);
        return this.tableConfigDao.changeStatus(statusCriteria);
    }

    @Override public Integer enable(String tableConfigIds) {
        StatusCriteria statusCriteria = new StatusCriteria(tableConfigIds, StatusRecord.ENABLE);
        this.tableConfigConverter.convertStatus(statusCriteria);

        return this.tableConfigDao.changeStatus(statusCriteria);
    }

    @Override public TableConfigBO getTableConfig(Long tableConfigId) {
        TableConfig tableConfig = this.tableConfigDao.getEntity(tableConfigId);
        return this.tableConfigConverter.po2bo(tableConfig);
    }

    @Override public List<TableConfigBO> queryTableConfigs(TableConfigQuery tableConfigQuery) {
        List<TableConfig> tableConfigList = this.tableConfigDao.queryTableConfigs(this.tableConfigConverter.toDbPagerQuery(tableConfigQuery));
        return this.tableConfigConverter.poList2BoList(tableConfigList);
    }

    @Override public Long getTableConfigCount(TableConfigQuery tableConfigQuery) {
        return this.tableConfigDao.countTableConfig(this.tableConfigConverter.toDbPagerQuery(tableConfigQuery));
    }
}