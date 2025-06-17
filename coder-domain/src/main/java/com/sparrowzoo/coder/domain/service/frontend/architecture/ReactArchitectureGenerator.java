package com.sparrowzoo.coder.domain.service.frontend.architecture;

import com.sparrowzoo.coder.domain.service.ArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;

import java.io.IOException;

public class ReactArchitectureGenerator implements ArchitectureGenerator {
    @Override
    public void generate(TableConfigRegistry registry, String tableName) throws IOException {

    }

    @Override
    public ArchitectureCategory getCategory() {
        return ArchitectureCategory.FRONTEND;
    }

    @Override
    public String template() {
        return "react";
    }
}
