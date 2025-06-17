package com.sparrowzoo.coder.moke.beans;

import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.po.ProjectConfig;
import com.sparrowzoo.coder.protocol.param.ProjectConfigParam;
import com.sparrowzoo.coder.protocol.query.ProjectConfigQuery;
import com.sparrowzoo.coder.repository.ProjectConfigRepository;

import javax.inject.Named;
import java.util.List;

@Named
public class MockProjectConfigRepository implements ProjectConfigRepository {
    @Override
    public Long save(ProjectConfigParam projectConfigParam) {
        return null;
    }

    @Override
    public Integer delete(String projectConfigIds) {
        return null;
    }

    @Override
    public Integer disable(String projectConfigIds) {
        return null;
    }

    @Override
    public Integer enable(String projectConfigIds) {
        return null;
    }

    @Override
    public ProjectConfigBO getProjectConfig(Long projectConfigId) {
        ProjectConfigBO projectConfig = new ProjectConfigBO();
        projectConfig.setWrapWithParent(false);
        projectConfig.setImplanted(true);
        projectConfig.setFrontendName("react-sparrow-admin");
        projectConfig.setChineseName("授权管理");
        projectConfig.setModulePrefix("coder");
        projectConfig.setScanPackage("com.sparrow");
        projectConfig.setDescription("");
        projectConfig.setId(1L);
        projectConfig.setName("sparrow-coder");
        projectConfig.setDescription("授权");
        projectConfig.setArchitectures("{\"FRONTEND\":\"react\",\"BACKEND\":\"springboot\"}");
        projectConfig.setFrontendName("react-sparrow-admin");
        projectConfig.setChineseName("授权管理");
        projectConfig.setDescription("");
        projectConfig.setModulePrefix("coder");
        projectConfig.setConfig("");
        projectConfig.setCreateUserId(1L);
        projectConfig.setModifiedUserId(0L);
        projectConfig.setGmtCreate(1L);
        projectConfig.setGmtModified(0L);
        projectConfig.setCreateUserName("harry");
        projectConfig.setModifiedUserName("");
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
