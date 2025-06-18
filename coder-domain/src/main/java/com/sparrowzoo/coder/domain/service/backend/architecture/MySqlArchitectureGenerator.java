package com.sparrowzoo.coder.domain.service.backend.architecture;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.orm.EntityManager;
import com.sparrow.utility.FileUtility;
import com.sparrowzoo.coder.domain.bo.ProjectConfigBO;
import com.sparrowzoo.coder.domain.service.AbstractArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.EnvConfig;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;

import javax.inject.Named;
import java.io.IOException;

@Named
public class MySqlArchitectureGenerator extends AbstractArchitectureGenerator {
    @Override
    public void generate(TableConfigRegistry registry, String tableName) throws IOException {
        EntityManager entityManager = registry.getTableContext(tableName).getEntityManager();
        EnvConfig envConfig = registry.getEnvConfig();
        ProjectConfigBO projectConfig = registry.getProject();
        String home = projectConfig.getImplanted() ? "" : String.valueOf(projectConfig.getCreateUserId());
        String fullPath = new FileNameBuilder(envConfig.getWorkspace())
                .joint(envConfig.getProjectRoot())
                .joint(home)
                .joint(projectConfig.getName())
                .joint("ddl")
                .fileName(tableName)
                .extension("sql")
                .build();
        String sql = entityManager.getCreateDDL();
        System.err.println(sql);
        FileUtility.getInstance().writeFile(fullPath, sql);
        System.err.printf("table create ddl write to %s\n", fullPath);
    }

    @Override
    public ArchitectureCategory getCategory() {
        return ArchitectureCategory.DATABASE;
    }

    @Override
    public String template() {
        return "mysql";
    }
}
