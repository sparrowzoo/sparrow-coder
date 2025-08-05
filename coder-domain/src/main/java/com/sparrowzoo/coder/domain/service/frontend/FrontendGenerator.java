package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import com.sparrowzoo.coder.enums.FrontendKey;

import java.io.IOException;

public interface FrontendGenerator {
    String getTargetPhysicalPath(FrontendKey key);
    String readTemplateContent(FrontendKey frontendKey);
    void generate(FrontendKey key, TableConfigRegistry registry) throws IOException;
}
