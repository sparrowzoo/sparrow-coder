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

package com.sparrow.coder.infrastructure.persistence.data.converter;

import com.sparrow.protocol.LoginUser;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.dao.StatusCriteria;
import com.sparrow.support.converter.POInitUtils;
import com.sparrow.coder.domain.bo.TableConfigBO;
import com.sparrow.coder.po.TableConfig;
import com.sparrow.coder.protocol.param.TableConfigParam;
import com.sparrow.coder.protocol.query.TableConfigQuery;
import com.sparrow.support.converter.PO2BOConverter;
import com.sparrow.support.converter.Param2POConverter;
import com.sparrow.coder.dao.query.TableConfigDBPagerQuery;
import java.util.ArrayList;
import java.util.List;
import com.sparrow.protocol.BeanCopier;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
public class TableConfigConverter implements Param2POConverter<TableConfigParam, TableConfig>, PO2BOConverter<TableConfigBO, TableConfig> {

    @Inject
    private BeanCopier beanCopier;

    public TableConfigDBPagerQuery toDbPagerQuery(TableConfigQuery tableConfigQuery) {
        if (tableConfigQuery == null) {
            return new TableConfigDBPagerQuery();
        }
        TableConfigDBPagerQuery tableConfig = new TableConfigDBPagerQuery();
        beanCopier.copyProperties(tableConfigQuery, tableConfig);
        return tableConfig;
    }

    @Override public TableConfig param2po(TableConfigParam param) {
        TableConfig tableConfig = new TableConfig();
        beanCopier.copyProperties(param, tableConfig);
        POInitUtils.init(tableConfig);

        return tableConfig;
    }

    @Override public TableConfigBO po2bo(TableConfig tableConfig) {
        TableConfigBO tableConfigBO = new TableConfigBO();
        beanCopier.copyProperties(tableConfig, tableConfigBO);

        return tableConfigBO;
    }

    @Override public List<TableConfigBO> poList2BoList(List<TableConfig> list) {
        List<TableConfigBO> tableConfigBos = new ArrayList<>(list.size());
        for (TableConfig tableConfig : list) {
            tableConfigBos.add(this.po2bo(tableConfig));
        }
        return tableConfigBos;
    }

    public void convertStatus(StatusCriteria statusCriteria) {
        LoginUser loginUser = SessionContext.getLoginUser();
        statusCriteria.setModifiedUserName(loginUser.getUserName());
        statusCriteria.setGmtModified(System.currentTimeMillis());
        statusCriteria.setModifiedUserId(loginUser.getUserId());
    }
}
