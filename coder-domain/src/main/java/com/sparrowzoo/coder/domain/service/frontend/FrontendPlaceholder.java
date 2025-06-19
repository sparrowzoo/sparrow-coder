package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrowzoo.coder.enums.FrontendKey;

public interface FrontendPlaceholder {
    String getPath(FrontendKey key);

    void init();
}
