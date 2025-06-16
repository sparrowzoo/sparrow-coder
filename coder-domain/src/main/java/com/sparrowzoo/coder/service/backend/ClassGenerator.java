package com.sparrow.coding.api.backend;

import com.sparrow.coding.enums.ClassKey;

import java.io.IOException;

public interface ClassGenerator {
    String getPackage( ClassKey classKey);
    String getClassName(ClassKey classKey);
    String getFullPhysicalPath( ClassKey classKey);
    String readConfigContent(String templateFileName);
    void generate(ClassKey classKey) throws IOException;
}
