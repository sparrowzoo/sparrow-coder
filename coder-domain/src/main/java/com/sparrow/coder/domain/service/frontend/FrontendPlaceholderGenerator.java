package com.sparrow.coder.domain.service.frontend;

import com.sparrow.coder.enums.FrontendKey;

public interface FrontendPlaceholderGenerator {
    String getPath(FrontendKey key);
}
