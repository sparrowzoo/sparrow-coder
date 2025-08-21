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

package com.sparrowzoo.coder.infrastructure.persistence.data.converter;

import com.sparrow.context.SessionContext;
import com.sparrow.protocol.LoginUser;
import com.sparrow.protocol.dao.StatusCriteria;
import com.sparrow.support.converter.POInitUtils;
import com.sparrowzoo.coder.domain.bo.UserExampleBO;
import com.sparrowzoo.coder.po.UserExample;
import com.sparrowzoo.coder.protocol.param.UserExampleParam;
import com.sparrowzoo.coder.protocol.query.UserExampleQuery;
import com.sparrow.support.converter.PO2BOConverter;
import com.sparrow.support.converter.Param2POConverter;
import com.sparrowzoo.coder.dao.query.UserExampleDBPagerQuery;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Inject;
import com.sparrow.protocol.BeanCopier;


@Named
public class UserExampleConverter implements Param2POConverter<UserExampleParam, UserExample>, PO2BOConverter<UserExampleBO, UserExample> {

    @Inject
    private BeanCopier beanCopier;

    public UserExampleDBPagerQuery toDbPagerQuery(UserExampleQuery userExampleQuery) {
           if (userExampleQuery == null) {
               return new UserExampleDBPagerQuery();
           }
           UserExampleDBPagerQuery userExample = new UserExampleDBPagerQuery();
           beanCopier.copyProperties(userExampleQuery, userExample);
           return userExample;
       }

    @Override public UserExample param2po(UserExampleParam param) {
        UserExample userExample = new UserExample();
        beanCopier.copyProperties(param, userExample);
        POInitUtils.init(userExample);

        return userExample;
    }

    @Override public UserExampleBO po2bo(UserExample userExample) {
        UserExampleBO userExampleBO = new UserExampleBO();
        beanCopier.copyProperties(userExample, userExampleBO);

        return userExampleBO;
    }

    @Override public List<UserExampleBO> poList2BoList(List<UserExample> list) {
        List<UserExampleBO> userExampleBos = new ArrayList<>(list.size());
        for (UserExample userExample : list) {
            userExampleBos.add(this.po2bo(userExample));
        }
        return userExampleBos;
    }

    public void convertStatus(StatusCriteria statusCriteria){
            LoginUser loginUser = SessionContext.getLoginUser();
            statusCriteria.setModifiedUserName(loginUser.getUserName());
            statusCriteria.setGmtModified(System.currentTimeMillis());
            statusCriteria.setModifiedUserId(loginUser.getUserId());
    }
}
