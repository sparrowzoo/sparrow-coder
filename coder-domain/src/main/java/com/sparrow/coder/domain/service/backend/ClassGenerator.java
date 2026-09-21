package com.sparrow.coder.domain.service.backend;


import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import com.sparrow.coder.enums.ClassKey;

import java.io.IOException;

public interface ClassGenerator {
    String getClassPhysicalPath(ClassKey classKey);

    String readTemplateContent(ClassKey classKey);

    void generate(ClassKey classKey, TableConfigRegistry registry) throws IOException;
}
