package com.sparrowzoo.coder.domain.service.registry;

import com.sparrow.container.FactoryBean;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.PlaceholderExtension;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class PlaceholderExtensionRegistry implements FactoryBean<PlaceholderExtension> {


    private PlaceholderExtensionRegistry() {
    }

    static class Inner {
        private static final PlaceholderExtensionRegistry placeholderExtensionRegistry = new PlaceholderExtensionRegistry();
    }

    public static PlaceholderExtensionRegistry getInstance() {
        return Inner.placeholderExtensionRegistry;
    }

    private Map<String, PlaceholderExtension> dependencyPlaceholderExtensionMap = new HashMap<>();


    private Map<String, PlaceholderExtension> placeholderExtensionMap = new HashMap<>();

    @Override
    public void pubObject(String s, PlaceholderExtension placeholderExtension) {
        if(placeholderExtension.getClass().getName().contains("dependency")){
            this.dependencyPlaceholderExtensionMap.put(s, placeholderExtension);
            return;
        }
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

    public void extension(TableContext tableContext, TableConfigRegistry registry) {
        for (String name : this.placeholderExtensionMap.keySet()) {
            PlaceholderExtension extension = this.placeholderExtensionMap.get(name);
            if (extension == null) {
                log.error("placeholder extension not found: " + name);
                continue;
            }
            extension.extend(tableContext, registry);
        }
    }

    public void dependencyExtension(TableContext tableContext, TableConfigRegistry registry) {
        for(String name : this.dependencyPlaceholderExtensionMap.keySet()){
            PlaceholderExtension extension = this.dependencyPlaceholderExtensionMap.get(name);
            if (extension == null) {
                log.error("placeholder extension not found: " + name);
                continue;
            }
            extension.extend(tableContext, registry);
        }
    }
}
