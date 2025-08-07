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

package com.sparrowzoo.coder.repository;
import com.sparrowzoo.coder.domain.bo.UserExampleBO;
import com.sparrowzoo.coder.protocol.param.UserExampleParam;
import com.sparrowzoo.coder.protocol.query.UserExampleQuery;
import java.util.List;
import java.util.Set;




public interface UserExampleRepository {
    Long save(UserExampleParam userExampleParam);

    Integer delete(Set<Long> userExampleIds);

    Integer disable(Set<Long> userExampleIds);

    Integer enable(Set<Long> userExampleIds);

    UserExampleBO getUserExample(Long userExampleId);

    List<UserExampleBO> queryUserExamples(UserExampleQuery userExampleQuery);

    Long getUserExampleCount(UserExampleQuery userExampleQuery);

}