package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrowzoo.coder.enums.FrontendKey;

import java.io.IOException;

public interface FrontendGenerator {
    String getFullPhysicalPath(FrontendKey key);
    String readConfigContent(String templateFileName);
    void generate(FrontendKey key) throws IOException;
}
