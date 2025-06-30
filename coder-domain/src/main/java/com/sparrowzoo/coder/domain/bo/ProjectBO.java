package com.sparrowzoo.coder.domain.bo;

import com.sparrowzoo.coder.domain.service.ArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.EnvConfig;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Data
public class ProjectBO {

    public ProjectBO(ProjectConfigBO projectConfig, Properties scaffoldConfig, EnvConfig envConfig) {
        this.projectConfig = projectConfig;
        this.scaffoldConfig = scaffoldConfig;
        this.envConfig = envConfig;
        this.architectures=new ProjectArchsBO(this.projectConfig.getArchitectures());
    }

    private ProjectConfigBO projectConfig;
    private Properties scaffoldConfig;
    private EnvConfig envConfig;
    private List<String> i18nList=new ArrayList<>();
    private ProjectArchsBO architectures;

    public ArchitectureGenerator getArchitecture(ArchitectureCategory architectureCategory){
        return this.architectures.getArch(architectureCategory);
    }
}
