package com.sparrowzoo.coder.moke.beans;

import com.sparrowzoo.coder.domain.service.EnvConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.io.File;

@Named
@Slf4j
public class EnvConfigImpl implements EnvConfig {
    EnvConfigImpl() {
        log.info("env config impl");
    }

    @Value("${workspace}")
    private String workspace;

    @Value("${project_root}")
    private String projectRoot;

    @Value("${frontend_project_root}")
    private String frontendProjectRoot;

    @Value("${multi_user}")
    private Boolean multiUser;


    @Override
    public String getWorkspace() {
        return this.workspace.replace("/", File.separator);
    }

    @Override
    public String getProjectRoot() {
        return this.projectRoot.replace("/", File.separator);
    }

    @Override
    public String getFrontendProjectRoot() {
        return frontendProjectRoot.replace("/", File.separator);
    }

    @Override
    public Boolean getMultiUser() {
        return this.multiUser;
    }

    @Override
    public String getHome(Long userId) {
        if (this.multiUser) {
            return userId + "";
        }
        return "";
    }


}
