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
import com.sparrowzoo.coder.protocol.dto.UserExampleDTO;
import com.sparrowzoo.coder.domain.bo.UserExampleBO;
import com.sparrow.utility.CollectionsUtility;
import java.util.*;
import javax.inject.*;
import com.sparrow.protocol.BeanCopier;


@Named
public class UserExampleAssemble{

    @Inject
    private BeanCopier beanCopier;

     public UserExampleDTO boAssembleDTO(UserExampleBO bo) {
        UserExampleDTO userExample = new UserExampleDTO();
        beanCopier.copyProperties(bo, userExample);
        return userExample;
    }

     public List<UserExampleDTO> boListAssembleDTOList(List<UserExampleBO> list) {
        if (CollectionsUtility.isNullOrEmpty(list)) {
            return Collections.emptyList();
        }
        List<UserExampleDTO> userExampleDTOList = new ArrayList<>(list.size());
        for (UserExampleBO userExampleBo : list) {
            userExampleDTOList.add(this.boAssembleDTO(userExampleBo));
        }
        return userExampleDTOList;
    }

    public PagerResult<UserExampleDTO> assemblePager(ListRecordTotalBO<UserExampleBO> userExampleListTotalRecord,
        SimplePager userExampleQuery) {
        List<UserExampleDTO> userExampleDTOList = this.boListAssembleDTOList(userExampleListTotalRecord.getList());
        PagerResult<UserExampleDTO> pagerResult = new PagerResult<>(userExampleQuery);
        pagerResult.setList(userExampleDTOList);
        pagerResult.setRecordTotal(userExampleListTotalRecord.getTotal());
        return pagerResult;
    }

}