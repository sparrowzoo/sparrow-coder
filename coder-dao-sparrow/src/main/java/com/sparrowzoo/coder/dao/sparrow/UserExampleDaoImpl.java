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
import com.sparrowzoo.coder.dao.UserExampleDAO;
import com.sparrowzoo.coder.dao.query.UserExampleDBPagerQuery;
import com.sparrowzoo.coder.po.UserExample;
import java.util.List;
import javax.inject.Named;
import com.sparrow.protocol.*;
import com.sparrow.context.SessionContext;
import com.sparrow.protocol.enums.StatusRecord;


@Named
public class UserExampleDaoImpl extends ORMStrategy<UserExample, Long> implements UserExampleDAO {
    @Override public List<UserExample> queryUserExamples(UserExampleDBPagerQuery pagerUserExampleQuery) {
        SearchCriteria searchCriteria = new SearchCriteria(pagerUserExampleQuery);
        searchCriteria.setWhere(this.generateCriteria(pagerUserExampleQuery));
        searchCriteria.setOrderCriteria(OrderCriteria.desc(UserExample::getId));
        return this.getList(searchCriteria);
    }

    private BooleanCriteria generateCriteria(UserExampleDBPagerQuery userExampleQuery) {
        BooleanCriteria booleanCriteria= BooleanCriteria.criteria(Criteria.field(UserExample::getUserName).equal(userExampleQuery.getUserName())).and(Criteria.field(UserExample::getChineseName).equal(userExampleQuery.getChineseName())).and(Criteria.field(UserExample::getCreateUserId).equal(SessionContext.getLoginUser().getUserId()));if(userExampleQuery.getStatus()!=null&&userExampleQuery.getStatus()>=0) {booleanCriteria.and(Criteria.field(UserExample::getStatus).equal(StatusRecord.valueOf(userExampleQuery.getStatus())));} return booleanCriteria;
    }

    @Override public Long countUserExample(UserExampleDBPagerQuery userExamplePagerQuery) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(this.generateCriteria(userExamplePagerQuery));
        return this.getCount(searchCriteria);
    }
}