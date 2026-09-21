package com.sparrow.coder.domain.service;

import com.sparrow.protocol.BusinessException;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;

import java.io.IOException;

public interface CodeGenerator {
    void generate(String tableName) throws IOException, BusinessException;

    void initScaffold();

    void clear();

    TableConfigRegistry getRegistry();

}
