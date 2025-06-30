package com.sparrowzoo.coder.domain.service;

import java.io.IOException;

public interface CodeGenerator {
    void generate(String tableName) throws IOException;

    void initScaffold();

    void clear();
}
