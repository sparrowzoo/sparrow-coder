package com.sparrow.coder.domain.service.backend;

import com.sparrow.coder.enums.ClassKey;

public interface ClassPlaceholderGenerator {
    String getModule(ClassKey classKey);

    String getPackage(ClassKey classKey);

    String getClassName(ClassKey classKey);
}
