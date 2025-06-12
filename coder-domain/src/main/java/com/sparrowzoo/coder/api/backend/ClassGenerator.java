package com.sparrow.coding.api.backend;

import com.sparrow.coding.enums.ClassKey;
import com.sparrow.coding.po.ProjectConfig;

import java.io.IOException;

public interface ClassGenerator {

    String getPackage(String tableName, ClassKey classKey);

    String getClassName(String tableName, ClassKey classKey);

    String getFullPhysicalPath(String tableName, ClassKey classKey);

    String readConfigContent(String templateFileName);
    void generate(String tableName, ClassKey classKey) throws IOException;
}
