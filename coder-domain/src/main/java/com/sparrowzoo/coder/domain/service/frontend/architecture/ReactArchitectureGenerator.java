package com.sparrowzoo.coder.domain.service.frontend.architecture;

import com.sparrowzoo.coder.constant.ArchitectureNames;
import com.sparrowzoo.coder.domain.service.AbstractArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.frontend.DefaultFrontendGenerator;
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
        FrontendGenerator frontendGenerator = new DefaultFrontendGenerator(registry, tableName, this.getName());
        frontendGenerator.generate(FrontendKey.PAGE);
        frontendGenerator.generate(FrontendKey.API);
        frontendGenerator.generate(FrontendKey.ADD);
        frontendGenerator.generate(FrontendKey.EDIT);
        frontendGenerator.generate(FrontendKey.SEARCH);
        frontendGenerator.generate(FrontendKey.OPERATION);
        frontendGenerator.generate(FrontendKey.SCHEMA);
        frontendGenerator.generate(FrontendKey.COLUMNS);
        frontendGenerator.generate(FrontendKey.MESSAGE);
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
