package com.sparrowzoo.coder.domain.service;

import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;

import java.io.IOException;

public interface CodeGenerator {
    void initRegistry(TableConfigRegistry registry) throws ClassNotFoundException, IOException;

    void generate(String tableName) throws IOException;

    void initScaffold();

    void clear();
}
