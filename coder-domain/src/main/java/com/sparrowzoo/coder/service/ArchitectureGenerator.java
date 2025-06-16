package com.sparrowzoo.coder.service;

import com.sparrowzoo.coder.enums.ArchitectureCategory;

import java.io.IOException;

public interface ArchitectureGenerator {
    void generate(String tableName) throws IOException;

    ArchitectureCategory getCategory();
}
