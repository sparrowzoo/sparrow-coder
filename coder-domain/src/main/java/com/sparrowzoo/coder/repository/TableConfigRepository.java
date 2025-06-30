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
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.protocol.param.TableConfigParam;
import com.sparrowzoo.coder.protocol.query.TableConfigQuery;
import java.util.List;
import java.util.Set;




public interface TableConfigRepository {
    Long save(TableConfigParam tableConfigParam);

    Integer delete(Set<Long> tableConfigIds);

    Integer disable(Set<Long> tableConfigIds);

    Integer enable(Set<Long> tableConfigIds);

    TableConfigBO getTableConfig(Long tableConfigId);

    List<TableConfigBO> queryTableConfigs(TableConfigQuery tableConfigQuery);

    Long getTableConfigCount(TableConfigQuery tableConfigQuery);

}