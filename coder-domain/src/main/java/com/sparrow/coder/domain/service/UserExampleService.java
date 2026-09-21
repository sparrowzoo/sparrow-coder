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

package com.sparrow.coder.domain.service;

import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.ListRecordTotalBO;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.utility.CollectionsUtility;
import com.sparrow.coder.domain.bo.UserExampleBO;
import com.sparrow.coder.protocol.param.UserExampleParam;
import com.sparrow.coder.protocol.query.UserExampleQuery;
import com.sparrow.coder.repository.UserExampleRepository;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Set;


@Named
public class UserExampleService {
    @Inject
    private UserExampleRepository userExampleRepository;

    private void validateSaveUserExample(UserExampleParam userExampleParam) throws BusinessException {
        //Asserts.isTrue(StringUtility.isNullOrEmpty(userExampleParam.getName()), SecurityAdminError.NAME_IS_EMPTY, UserExampleSuffix.name);
    }

    public Long saveUserExample(UserExampleParam userExampleParam) throws BusinessException {
        this.validateSaveUserExample(userExampleParam);
        return this.userExampleRepository.save(userExampleParam);
    }

    public Integer deleteUserExample(Set<Long> userExampleIds) throws BusinessException {
        Asserts.isTrue(CollectionsUtility.isNullOrEmpty(userExampleIds), SparrowError.GLOBAL_PARAMETER_NULL);
        return this.userExampleRepository.delete(userExampleIds);
    }

    public Integer enableUserExample(Set<Long> userExampleIds) throws BusinessException {
        Asserts.isTrue(CollectionsUtility.isNullOrEmpty(userExampleIds), SparrowError.GLOBAL_PARAMETER_NULL);
        return this.userExampleRepository.enable(userExampleIds);
    }

    public Integer disableUserExample(Set<Long> userExampleIds) throws BusinessException {
        Asserts.isTrue(CollectionsUtility.isNullOrEmpty(userExampleIds), SparrowError.GLOBAL_PARAMETER_NULL);
        return this.userExampleRepository.disable(userExampleIds);
    }

    public ListRecordTotalBO<UserExampleBO> queryAllUserExample() {
        return queryUserExample(null);
    }

    public ListRecordTotalBO<UserExampleBO> queryUserExample(UserExampleQuery userExampleQuery) {
        Long totalRecord = this.userExampleRepository.getUserExampleCount(userExampleQuery);
        List<UserExampleBO> userExampleBoList = null;
        if (totalRecord > 0) {
            userExampleBoList = this.userExampleRepository.queryUserExamples(userExampleQuery);
        }
        return new ListRecordTotalBO<>(userExampleBoList, totalRecord);
    }

    public UserExampleBO getUserExample(Long userExampleId) throws BusinessException {
         Asserts.isTrue(userExampleId==null, SparrowError.GLOBAL_PARAMETER_NULL);
        return this.userExampleRepository.getUserExample(userExampleId);
    }


}
