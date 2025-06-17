package com.sparrowzoo.coder.service;

import java.io.IOException;

public interface CodeGenerator {
    void initRegistry(Long projectId,EnvConfig envConfig) throws ClassNotFoundException, IOException;

    void generate(String tableName) throws IOException;

    void initScaffold();

    void clear();
}
