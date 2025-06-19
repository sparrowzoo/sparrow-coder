package com.sparrowzoo.coder.domain.service.registry;

import com.sparrow.container.FactoryBean;
import com.sparrowzoo.coder.domain.service.frontend.generator.column.ColumnGenerator;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Named
public class ColumnGeneratorRegistry implements FactoryBean<ColumnGenerator> {
    private Map<String, ColumnGenerator> container = new HashMap<>();

    @Override
    public void pubObject(String s, ColumnGenerator columnGenerator) {
        this.pubObject(s, columnGenerator);
    }

    @Override
    public ColumnGenerator getObject(String s) {
        return this.container.get(s);
    }

    @Override
    public Class<?> getObjectType() {
        return ColumnGenerator.class;
    }

    @Override
    public void removeObject(String s) {
        this.container.remove(s);
    }

    @Override
    public Iterator<String> keyIterator() {
        return this.container.keySet().iterator();
    }
}
