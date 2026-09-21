package com.sparrow.coder.domain.service;

import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;

public interface PlaceholderExtension {
    void extend(TableContext tableContext, TableConfigRegistry registry);
}
