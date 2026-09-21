package com.sparrow.coder.domain.service.frontend.generator.column;

import com.sparrow.utility.StringUtility;
import com.sparrow.coder.domain.service.registry.ColumnGeneratorRegistry;
import org.springframework.beans.factory.InitializingBean;


public abstract class AbstractColumnGenerator implements ColumnGenerator, InitializingBean {
    private final ColumnGeneratorRegistry columnGeneratorRegistry = ColumnGeneratorRegistry.getInstance();

    protected String generate(String id, String defaultId, String format) {
        String componentId = StringUtility.isNullOrEmpty(id) ? defaultId : id;
        return String.format(format, componentId);
    }

    @Override
    public void afterPropertiesSet() {
        this.columnGeneratorRegistry.pubObject(this.getName(), this);
    }
}
