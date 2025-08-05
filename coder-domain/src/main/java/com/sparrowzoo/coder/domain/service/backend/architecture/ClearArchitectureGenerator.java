package com.sparrowzoo.coder.domain.service.backend.architecture;

import com.sparrowzoo.coder.constant.ArchitectureNames;
import com.sparrowzoo.coder.domain.bo.TableConfigBO;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.backend.ClassGenerator;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.enums.ClassKey;
import com.sparrowzoo.coder.enums.CodeSource;

import javax.inject.Named;
import java.io.IOException;

@Named
public class ClearArchitectureGenerator extends AbstractArchitectureGenerator {

    @Override
    public void generate(TableConfigRegistry registry, String tableName) throws IOException {
        TableContext context = registry.getTableContext(tableName);
        ClassGenerator classGenerator =context.getClassGenerator();
        TableConfigBO tableConfig = context.getTableConfig();
        if (CodeSource.SOURCE_CODE.getIdentity().equals(tableConfig.getSource())) {
            classGenerator.generate(ClassKey.PO,registry);
        }
        classGenerator.generate(ClassKey.BO,registry);
        classGenerator.generate(ClassKey.QUERY,registry);
        classGenerator.generate(ClassKey.PARAM,registry);
        classGenerator.generate(ClassKey.DTO,registry);
        classGenerator.generate(ClassKey.DAO,registry);
        classGenerator.generate(ClassKey.DAO_IMPL,registry);
        classGenerator.generate(ClassKey.DAO_MYBATIS,registry);
        classGenerator.generate(ClassKey.DATA_CONVERTER,registry);
        classGenerator.generate(ClassKey.SERVICE,registry);
        classGenerator.generate(ClassKey.REPOSITORY,registry);
        classGenerator.generate(ClassKey.REPOSITORY_IMPL,registry);
        classGenerator.generate(ClassKey.ASSEMBLE,registry);
        classGenerator.generate(ClassKey.CONTROLLER,registry);
        classGenerator.generate(ClassKey.PAGER_QUERY,registry);
    }

    @Override
    public ArchitectureCategory getCategory() {
        return ArchitectureCategory.BACKEND;
    }

    @Override
    public String getName() {
        return ArchitectureNames.CLEAR;
    }
}
