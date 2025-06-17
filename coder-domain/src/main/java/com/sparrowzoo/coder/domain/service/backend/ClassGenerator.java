package com.sparrowzoo.coder.domain.service.backend;


import com.sparrowzoo.coder.enums.ClassKey;

import java.io.IOException;

public interface ClassGenerator {
    String getPackage(ClassKey classKey);

    String getClassName(ClassKey classKey);

    String getFullPhysicalPath(ClassKey classKey);

    String readConfigContent(String arch,String templateFileName);

    void generate(String arch,ClassKey classKey) throws IOException;
}
