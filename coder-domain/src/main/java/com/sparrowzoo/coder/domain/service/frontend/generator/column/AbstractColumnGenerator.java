package com.sparrowzoo.coder.domain.service.frontend.generator.column;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.service.registry.ColumnGeneratorRegistry;
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;


public abstract class AbstractColumnGenerator implements ColumnGenerator, InitializingBean {

    @Inject
    private ColumnGeneratorRegistry columnGeneratorRegistry;

    protected String generate(String id,String defaultId,String format) {
        String componentId = StringUtility.isNullOrEmpty(id)?defaultId:id;
        return String.format(format,componentId);
    }
    @Override
    public void afterPropertiesSet() {
        this.columnGeneratorRegistry.pubObject(this.getClass().getSimpleName(), this);
    }
}
