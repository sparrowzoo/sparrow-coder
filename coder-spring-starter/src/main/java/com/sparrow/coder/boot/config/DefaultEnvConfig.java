package com.sparrow.coder.boot.config;

import com.sparrow.coder.domain.service.EnvConfig;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

@Named
@Slf4j
public class DefaultEnvConfig implements EnvConfig {
    DefaultEnvConfig() {
        log.info("env config impl");
    }

    @Value("${workspace}")
    private String workspace;

    @Value("${project_root}")
    private String projectRoot;
    
    @Value("${multi_user}")
    private Boolean multiUser;

    @Value("${overwrite}")
    private Boolean overwrite;


    @Override
    public String getWorkspace() {
        return this.workspace.replace("/", File.separator);
    }

    @Override
    public String getProjectRoot() {
        return this.projectRoot.replace("/", File.separator);
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

    @Override
    public Boolean overwrite() {
        return this.overwrite;
    }
}
