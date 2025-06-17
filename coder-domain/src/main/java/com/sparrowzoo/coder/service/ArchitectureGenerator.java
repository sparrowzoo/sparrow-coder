package com.sparrowzoo.coder.service;

import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.service.registry.TableConfigRegistry;

import java.io.IOException;

public interface ArchitectureGenerator {
    public void generate(TableConfigRegistry registry, String tableName) throws IOException;
    ArchitectureCategory getCategory();
}
