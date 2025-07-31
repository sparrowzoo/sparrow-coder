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

import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;
import com.sparrowzoo.coder.dao.ProjectConfigDAO;
import com.sparrowzoo.coder.dao.query.ProjectConfigDBPagerQuery;
import com.sparrowzoo.coder.po.ProjectConfig;
import java.util.List;
import javax.inject.Named;
import com.sparrow.protocol.*;
import com.sparrow.protocol.enums.StatusRecord;


@Named
public class ProjectConfigDaoImpl extends ORMStrategy<ProjectConfig, Long> implements ProjectConfigDAO {
    @Override public List<ProjectConfig> queryProjectConfigs(ProjectConfigDBPagerQuery pagerProjectConfigQuery) {
        SearchCriteria searchCriteria = new SearchCriteria(pagerProjectConfigQuery);
        searchCriteria.setWhere(this.generateCriteria(pagerProjectConfigQuery));
        return this.getList(searchCriteria);
    }

    private BooleanCriteria generateCriteria(ProjectConfigDBPagerQuery projectConfigQuery) {
        BooleanCriteria booleanCriteria= BooleanCriteria.criteria(Criteria.field(ProjectConfig::getName).equal(projectConfigQuery.getName())).and(Criteria.field(ProjectConfig::getFrontendName).equal(projectConfigQuery.getFrontendName()));if(projectConfigQuery.getStatus()!=null&&projectConfigQuery.getStatus()>=0) {booleanCriteria.and(Criteria.field(ProjectConfig::getStatus).equal(StatusRecord.valueOf(projectConfigQuery.getStatus())));}return booleanCriteria;
    }

    @Override public Long countProjectConfig(ProjectConfigDBPagerQuery projectConfigPagerQuery) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(this.generateCriteria(projectConfigPagerQuery));
        return this.getCount(searchCriteria);
    }
}