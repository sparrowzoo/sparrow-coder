package com.sparrow.coding.api;

import java.io.IOException;

public interface CodeGenerator {
    void initRegistry(Long projectId) throws ClassNotFoundException, IOException;

    void generate(String tableName) throws IOException;

    void initScaffold();
}
