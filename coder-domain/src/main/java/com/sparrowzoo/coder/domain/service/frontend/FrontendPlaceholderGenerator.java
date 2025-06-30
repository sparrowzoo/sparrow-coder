package com.sparrowzoo.coder.domain.service.frontend;

import com.sparrowzoo.coder.enums.FrontendKey;

public interface FrontendPlaceholderGenerator {
    String getPath(FrontendKey key);
}
