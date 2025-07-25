package com.sparrowzoo.coder.moke.beans;

import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.protocol.param.ProjectConfigParam;
import com.sparrowzoo.coder.protocol.query.ProjectConfigQuery;
import com.sparrowzoo.coder.repository.ProjectConfigRepository;

import javax.inject.Named;
import java.util.List;
import java.util.Set;

@Named
public class MockProjectConfigRepository implements ProjectConfigRepository {
    @Override
    public Long save(ProjectConfigParam projectConfigParam) {
        return null;
    }

    @Override
    public Integer delete(Set<Long> projectConfigIds) {
        return null;
    }

    @Override
    public Integer disable(Set<Long> projectConfigIds) {
        return null;
    }

    @Override
    public Integer enable(Set<Long> projectConfigIds) {
        return null;
    }

    @Override
    public ProjectConfigBO getProjectConfig(Long projectConfigId) {
        ProjectConfigBO projectConfig = new ProjectConfigBO();
        projectConfig.setWrapWithParent(false);
        projectConfig.setFrontendName("react-next-admin");
        projectConfig.setChineseName("代码生成器模拟");
        projectConfig.setModulePrefix("coder");
        projectConfig.setDescription("代码生成器模拟");
        projectConfig.setI18n(true);
        projectConfig.setId(1L);
        projectConfig.setName("sparrow-coder");
        projectConfig.setArchitectures("{\"FRONTEND\":\"react\",\"DATABASE\":\"mysql\",\"BACKEND\":\"clear\"}");

        projectConfig.setConfig("");
        projectConfig.setCreateUserId(1L);
        projectConfig.setModifiedUserId(1L);
        projectConfig.setGmtCreate(0L);
        projectConfig.setGmtModified(0L);
        projectConfig.setCreateUserName("harry");
        projectConfig.setModifiedUserName("harry");
        return projectConfig;
    }

    @Override
    public List<ProjectConfigBO> queryProjectConfigs(ProjectConfigQuery projectConfigQuery) {
        return null;
    }

    @Override
    public Long getProjectConfigCount(ProjectConfigQuery projectConfigQuery) {
        return null;
    }
}
