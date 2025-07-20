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
        ClassGenerator classGenerator = context.getClassGenerator();
        TableConfigBO tableConfig = context.getTableConfig();
        if (CodeSource.SOURCE_CODE.name().equals(tableConfig.getSource())) {
            classGenerator.generate(ClassKey.PO);
        }
        classGenerator.generate(ClassKey.BO);
        classGenerator.generate(ClassKey.QUERY);
        classGenerator.generate(ClassKey.PARAM);
        classGenerator.generate(ClassKey.DTO);
        classGenerator.generate(ClassKey.DAO);
        classGenerator.generate(ClassKey.DAO_IMPL);
        classGenerator.generate(ClassKey.DAO_MYBATIS);
        classGenerator.generate(ClassKey.DATA_CONVERTER);
        classGenerator.generate(ClassKey.SERVICE);
        classGenerator.generate(ClassKey.REPOSITORY);
        classGenerator.generate(ClassKey.REPOSITORY_IMPL);
        classGenerator.generate(ClassKey.ASSEMBLE);
        classGenerator.generate(ClassKey.CONTROLLER);
        classGenerator.generate(ClassKey.PAGER_QUERY);
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
