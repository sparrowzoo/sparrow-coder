package com.sparrowzoo.coder.domain.service.registry;

import com.sparrow.container.FactoryBean;
import com.sparrowzoo.coder.domain.service.frontend.generator.column.ColumnGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ColumnGeneratorRegistry implements FactoryBean<ColumnGenerator> {
    private Map<String, ColumnGenerator> container = new HashMap<>();

    private ColumnGeneratorRegistry() {
    }

    @Override
    public void pubObject(String s, ColumnGenerator columnGenerator) {
        this.container.put(s, columnGenerator);
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


    static class Inner {
        private static final ColumnGeneratorRegistry columnGeneratorRegistry = new ColumnGeneratorRegistry();
    }

    public static ColumnGeneratorRegistry getInstance() {
        return Inner.columnGeneratorRegistry;
    }


}
