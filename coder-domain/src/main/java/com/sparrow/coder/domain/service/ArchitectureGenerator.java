package com.sparrow.coder.domain.service;

import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.ArchitectureCategory;

import java.io.IOException;

public interface ArchitectureGenerator {
    void generate(TableConfigRegistry registry, String tableName) throws IOException;

    ArchitectureCategory getCategory();

    String getName();
}
