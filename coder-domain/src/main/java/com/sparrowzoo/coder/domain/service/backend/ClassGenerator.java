package com.sparrowzoo.coder.domain.service.backend;


import com.sparrowzoo.coder.enums.ClassKey;

import java.io.IOException;

public interface ClassGenerator {
    String getFullPhysicalPath(ClassKey classKey);

    String readConfigContent(String templateFileName);

    void generate(ClassKey classKey) throws IOException;
}
