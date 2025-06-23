package com.sparrowzoo.coder.domain.service.backend.architecture;

import com.sparrowzoo.coder.domain.service.AbstractArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;

import java.io.IOException;

public class MvcArchitectureGenerator extends AbstractArchitectureGenerator {
    @Override
    public void generate(TableConfigRegistry registry, String tableName) throws IOException {

    }

    @Override
    public ArchitectureCategory getCategory() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
