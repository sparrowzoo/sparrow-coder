package com.sparrowzoo.coder.domain.service.backend;

import com.sparrowzoo.coder.enums.ClassKey;

public interface ClassPlaceholder {
    String getModule(ClassKey classKey);

    String getPackage(ClassKey classKey);

    String getClassName(ClassKey classKey);

    void init();
}
