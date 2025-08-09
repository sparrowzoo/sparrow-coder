package com.sparrowzoo.coder.domain.service.frontend.architecture;

import com.sparrowzoo.coder.constant.ArchitectureNames;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.AbstractArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.frontend.FrontendGenerator;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.enums.FrontendKey;

import javax.inject.Named;
import java.io.IOException;

@Named
public class ReactArchitectureGenerator extends AbstractArchitectureGenerator {
    @Override
    public void generate(TableConfigRegistry registry, String tableName) throws IOException {
        TableContext tableContext = registry.getTableContext(tableName);
        FrontendGenerator frontendGenerator = tableContext.getFrontendGenerator();
        frontendGenerator.generate(FrontendKey.PAGE, registry);
        frontendGenerator.generate(FrontendKey.API, registry);
        frontendGenerator.generate(FrontendKey.ADD, registry);
        frontendGenerator.generate(FrontendKey.EDIT, registry);
        frontendGenerator.generate(FrontendKey.SEARCH, registry);
        frontendGenerator.generate(FrontendKey.OPERATION, registry);
        frontendGenerator.generate(FrontendKey.SCHEMA, registry);
        frontendGenerator.generate(FrontendKey.COLUMNS, registry);
        frontendGenerator.generate(FrontendKey.MESSAGE, registry);
        frontendGenerator.generate(FrontendKey.MESSAGE_FILE_LIST, registry);
    }

    @Override
    public ArchitectureCategory getCategory() {
        return ArchitectureCategory.FRONTEND;
    }

    @Override
    public String getName() {
        return ArchitectureNames.REACT;
    }
}
