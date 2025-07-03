package com.sparrowzoo.coder.domain.service;

import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;

import java.io.IOException;

public interface CodeGenerator {
    void generate(String tableName) throws IOException;

    void initScaffold();

    void clear();

    TableConfigRegistry getRegistry();

}
