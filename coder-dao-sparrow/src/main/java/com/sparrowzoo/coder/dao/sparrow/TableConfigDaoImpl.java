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

package com.sparrowzoo.coder.dao.sparrow;

import com.sparrow.orm.query.*;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrowzoo.coder.dao.TableConfigDAO;
import com.sparrowzoo.coder.dao.query.TableConfigDBPagerQuery;
import com.sparrowzoo.coder.po.TableConfig;
import java.util.List;
import javax.inject.Named;
import com.sparrow.protocol.*;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.enums.StatusRecord;


@Named
public class TableConfigDaoImpl extends ORMStrategy<TableConfig, Long> implements TableConfigDAO {
    @Override public List<TableConfig> queryTableConfigs(TableConfigDBPagerQuery pagerTableConfigQuery) {
        SearchCriteria searchCriteria = new SearchCriteria(pagerTableConfigQuery);
        searchCriteria.setWhere(this.generateCriteria(pagerTableConfigQuery));
        searchCriteria.setOrderCriteria(OrderCriteria.desc(TableConfig::getId));
        return this.getList(searchCriteria);
    }

    private BooleanCriteria generateCriteria(TableConfigDBPagerQuery tableConfigQuery) {
        BooleanCriteria booleanCriteria= BooleanCriteria.criteria(Criteria.field(TableConfig::getTableName).equal(tableConfigQuery.getTableName())).and(Criteria.field(TableConfig::getClassName).equal(tableConfigQuery.getClassName())).and(Criteria.field(TableConfig::getCreateUserId).equal(SessionContext.getLoginUser().getUserId()));if(tableConfigQuery.getStatus()!=null&&tableConfigQuery.getStatus()>=0) {booleanCriteria.and(Criteria.field(TableConfig::getStatus).equal(StatusRecord.valueOf(tableConfigQuery.getStatus())));} return booleanCriteria;
    }

    @Override public Long countTableConfig(TableConfigDBPagerQuery tableConfigPagerQuery) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(this.generateCriteria(tableConfigPagerQuery));
        return this.getCount(searchCriteria);
    }
}
