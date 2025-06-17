package com.sparrowzoo.coder.service.backend.db;

import com.sparrow.io.file.FileNameBuilder;
import com.sparrow.orm.EntityManager;
import com.sparrow.utility.FileUtility;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.po.ProjectConfig;
import com.sparrowzoo.coder.service.AbstractArchitectureGenerator;
import com.sparrowzoo.coder.service.EnvConfig;
import com.sparrowzoo.coder.service.registry.TableConfigRegistry;

import javax.inject.Named;
import java.io.IOException;

@Named
public class MySqlArchitectureGenerator extends AbstractArchitectureGenerator {
    @Override
    public void generate(TableConfigRegistry registry, String tableName) throws IOException {
        EntityManager entityManager= registry.getTableContext(tableName).getEntityManager();
        EnvConfig envConfig = registry.getEnvConfig();
        ProjectConfig projectConfig = registry.getProjectConfig();
        String fullPath = new FileNameBuilder(envConfig.getWorkspace())
                .joint(String.valueOf(projectConfig.getCreateUserId()))
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
}
