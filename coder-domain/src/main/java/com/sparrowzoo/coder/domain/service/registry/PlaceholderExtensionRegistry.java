package com.sparrowzoo.coder.domain.service.registry;

import com.sparrow.container.FactoryBean;
import com.sparrowzoo.coder.domain.bo.CoderTriple;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.PlaceholderExtension;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlaceholderExtensionRegistry implements FactoryBean<PlaceholderExtension> {


    private PlaceholderExtensionRegistry() {}

    static class Inner {
        private static final PlaceholderExtensionRegistry placeholderExtensionRegistry = new PlaceholderExtensionRegistry();
    }

    public static PlaceholderExtensionRegistry getInstance() {
        return Inner.placeholderExtensionRegistry;
    }


    private Map<String, PlaceholderExtension> placeholderExtensionMap = new HashMap<>();

    @Override
    public void pubObject(String s, PlaceholderExtension placeholderExtension) {
        this.placeholderExtensionMap.put(s, placeholderExtension);
    }

    @Override
    public PlaceholderExtension getObject(String s) {
        return this.placeholderExtensionMap.get(s);
    }

    @Override
    public Class<?> getObjectType() {
        return PlaceholderExtension.class;
    }

    @Override
    public void removeObject(String s) {
        this.placeholderExtensionMap.remove(s);
    }

    @Override
    public Iterator<String> keyIterator() {
        return this.placeholderExtensionMap.keySet().iterator();
    }

    public void extension(TableContext tableContext,TableConfigRegistry registry){
        for(String name:this.placeholderExtensionMap.keySet()){
            PlaceholderExtension extension = this.placeholderExtensionMap.get(name);
            extension.extend(tableContext,registry);
        }
    }
}
