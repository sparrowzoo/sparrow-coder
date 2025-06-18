package com.sparrowzoo.coder.domain.service.backend;

import com.sparrowzoo.coder.enums.ClassKey;

public interface ClassMetaAccessor {
    String getPackage(ClassKey classKey);

    String getClassName(ClassKey classKey);

    String getModule(ClassKey key);
}
