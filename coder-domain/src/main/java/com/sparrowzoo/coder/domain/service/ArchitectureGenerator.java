package com.sparrowzoo.coder.domain.service;

import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;

import java.io.IOException;

public interface ArchitectureGenerator {
    void generate(TableConfigRegistry registry, String tableName) throws IOException;
    ArchitectureCategory getCategory();
    String getName();
}
