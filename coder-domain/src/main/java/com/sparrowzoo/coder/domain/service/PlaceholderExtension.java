package com.sparrowzoo.coder.domain.service;

import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;

public interface PlaceholderExtension {
    void extend(TableContext tableContext, TableConfigRegistry registry);
}
