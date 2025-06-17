package com.sparrowzoo.coder.service.backend.clear;

import com.sparrowzoo.coder.bo.TableContext;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.enums.ClassKey;
import com.sparrowzoo.coder.enums.CodeSource;
import com.sparrowzoo.coder.po.TableConfig;
import com.sparrowzoo.coder.service.AbstractArchitectureGenerator;
import com.sparrowzoo.coder.service.backend.ClassGenerator;
import com.sparrowzoo.coder.service.backend.DefaultClassGenerator;
import com.sparrowzoo.coder.service.registry.TableConfigRegistry;

import javax.inject.Named;
import java.io.IOException;

@Named
public class ClearArchitectureGenerator extends AbstractArchitectureGenerator {
    @Override
    public void generate(TableConfigRegistry registry,String tableName) throws IOException {
        ClassGenerator classGenerator = new DefaultClassGenerator(registry, tableName);
        TableContext context = registry.getTableContext(tableName);
        TableConfig tableConfig = context.getTableConfig();
        if (CodeSource.UPLOAD.name().equals(tableConfig.getSource())) {
            classGenerator.generate(ClassKey.PO);
        }
        classGenerator.generate(ClassKey.BO);
//        classGenerator.generate(ClassKey.QUERY);
//        classGenerator.generate(ClassKey.PARAM);
//        classGenerator.generate(ClassKey.VO);
//        classGenerator.generate(ClassKey.DAO);
//        classGenerator.generate(ClassKey.DAO_IMPL);
//        classGenerator.generate(ClassKey.DAO_MYBATIS);
//        classGenerator.generate(ClassKey.DATA_CONVERTER);
//        classGenerator.generate(ClassKey.SERVICE);
//        classGenerator.generate(ClassKey.REPOSITORY);
//        classGenerator.generate(ClassKey.REPOSITORY_IMPL);
//        classGenerator.generate(ClassKey.ASSEMBLE);
//        classGenerator.generate(ClassKey.CONTROLLER);
//        classGenerator.generate(ClassKey.PAGER_QUERY);
    }

    @Override
    public ArchitectureCategory getCategory() {
        return ArchitectureCategory.BACKEND;
    }
}
