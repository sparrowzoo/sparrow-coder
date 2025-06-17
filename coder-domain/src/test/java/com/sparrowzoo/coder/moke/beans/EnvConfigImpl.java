package com.sparrowzoo.coder.moke.beans;

import com.sparrowzoo.coder.service.EnvConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;

@Named
@Slf4j
public class EnvConfigImpl implements EnvConfig {
    EnvConfigImpl() {
        log.info("env config impl");
    }

    @Value("${workspace}")
    private String workspace;

    @Value("${scaffold}")
    private String scaffold;

    @Value("${project_root}")
    private String projectRoot;

    @Override
    public String getWorkspace() {
        return this.workspace;
    }

    @Override
    public String getProjectRoot() {
        return this.projectRoot;
    }

    @Override
    public String getScaffold() {
        return this.scaffold;
    }
}
