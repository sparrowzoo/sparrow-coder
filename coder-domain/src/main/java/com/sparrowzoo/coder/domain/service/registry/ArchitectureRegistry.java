package com.sparrowzoo.coder.domain.service.registry;

import com.sparrowzoo.coder.domain.service.ArchitectureGenerator;
import com.sparrowzoo.coder.enums.ArchitectureCategory;

import java.util.HashMap;
import java.util.Map;

public class ArchitectureRegistry {
    private Map<ArchitectureCategory, Map<String, ArchitectureGenerator>> registry;

    public void register(String name, ArchitectureGenerator generator) {
        registry.putIfAbsent(generator.getCategory(), new HashMap<>());
        registry.get(generator.getCategory()).put(name, generator);
    }

    public Map<ArchitectureCategory, Map<String, ArchitectureGenerator>> getRegistry() {
        return registry;
    }

    public ArchitectureGenerator getGenerator(ArchitectureCategory category, String name) {
        Map<String, ArchitectureGenerator> architectureMap = registry.get(category);
        if (architectureMap.containsKey(name)) {
            return architectureMap.get(name);
        }
        return architectureMap.values().iterator().next();
    }

    private ArchitectureRegistry() {
        registry = new HashMap<>();
    }


    static class Inner {
        private static final ArchitectureRegistry architectureRegistry = new ArchitectureRegistry();
    }

    public static ArchitectureRegistry getInstance() {
        return ArchitectureRegistry.Inner.architectureRegistry;
    }

}
