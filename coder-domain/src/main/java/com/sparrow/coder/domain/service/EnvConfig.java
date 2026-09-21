package com.sparrow.coder.domain.service;

public interface EnvConfig {
    public String getWorkspace();

    public String getProjectRoot();

    public Boolean getMultiUser();

    public String getHome(Long userId);

    public Boolean overwrite();

}
