package com.sparrow.coder.moke.beans;

import com.sparrow.coder.domain.service.EnvConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import jakarta.inject.Named;
import java.io.File;

@Named
@Slf4j
public class EnvConfigImpl implements EnvConfig {
    public EnvConfigImpl() {
        log.info("env config impl");
    }

    @Value("${scaffold_workspace:}")
    private String workspace;

    @Value("${scaffold_backend_template_home:}")
    private String backendScaffoldTemplateHome;

    @Value("${scaffold_frontend_template_home:")
    private String frontendScaffoldTemplateHome;

    @Value("${multi_user:false}")
    private Boolean multiUser;

    @Value("${overwrite:true}")
    private Boolean overwrite;

    @Value("${scaffold_frontend_home:}")
    private String scaffoldFrontendHome;


    @Override
    public String getScaffoldWorkspace() {
        return this.workspace;
    }

    @Override
    public String getBackendScaffoldTemplateHome() {
        return this.backendScaffoldTemplateHome;
    }

    @Override
    public String getFrontendScaffoldTemplateHome() {
        return this.frontendScaffoldTemplateHome;
    }

    @Override
    public String getScaffoldFrontendHome() {
        return this.scaffoldFrontendHome;
    }

    @Override
    public Boolean getMultiUser() {
        return this.multiUser;
    }

    @Override
    public String getUserHome(Long userId) {
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
