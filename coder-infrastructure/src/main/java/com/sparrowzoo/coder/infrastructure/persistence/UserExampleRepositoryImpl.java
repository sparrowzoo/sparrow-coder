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
import com.sparrowzoo.coder.dao.UserExampleDAO;
import com.sparrowzoo.coder.infrastructure.persistence.data.converter.UserExampleConverter;
import com.sparrowzoo.coder.po.UserExample;
import com.sparrowzoo.coder.domain.bo.UserExampleBO;
import com.sparrowzoo.coder.protocol.param.UserExampleParam;
import com.sparrowzoo.coder.repository.UserExampleRepository;
import com.sparrowzoo.coder.protocol.query.UserExampleQuery;

import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class UserExampleRepositoryImpl implements UserExampleRepository {
    @Inject
    private UserExampleConverter userExampleConverter;

    @Inject
    private UserExampleDAO userExampleDao;

    @Override public Long save(UserExampleParam userExampleParam) {
        UserExample userExample = this.userExampleConverter.param2po(userExampleParam);
        if (userExample.getId() != null) {
            this.userExampleDao.update(userExample);
            return userExample.getId();
        }
        this.userExampleDao.insert(userExample);
        return userExample.getId();
    }

    @Override public Integer delete(Set<Long> userExampleIds) {
        return this.userExampleDao.batchDelete(userExampleIds);
    }

    @Override public Integer disable(Set<Long> userExampleIds) {
        StatusCriteria<Long> statusCriteria = new StatusCriteria(userExampleIds, StatusRecord.DISABLE);
        this.userExampleConverter.convertStatus(statusCriteria);
        return this.userExampleDao.changeStatus(statusCriteria);
    }

    @Override public Integer enable(Set<Long> userExampleIds) {
        StatusCriteria<Long> statusCriteria = new StatusCriteria(userExampleIds, StatusRecord.ENABLE);
        this.userExampleConverter.convertStatus(statusCriteria);
        return this.userExampleDao.changeStatus(statusCriteria);
    }

    @Override public UserExampleBO getUserExample(Long userExampleId) {
        UserExample userExample = this.userExampleDao.getEntity(userExampleId);
        return this.userExampleConverter.po2bo(userExample);
    }

    @Override public List<UserExampleBO> queryUserExamples(UserExampleQuery userExampleQuery) {
        List<UserExample> userExampleList = this.userExampleDao.queryUserExamples(this.userExampleConverter.toDbPagerQuery(userExampleQuery));
        return this.userExampleConverter.poList2BoList(userExampleList);
    }

    @Override public Long getUserExampleCount(UserExampleQuery userExampleQuery) {
        return this.userExampleDao.countUserExample(this.userExampleConverter.toDbPagerQuery(userExampleQuery));
    }
}