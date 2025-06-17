package com.sparrowzoo.coder.service.registry;

import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.service.ArchitectureGenerator;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

public class ArchitectureRegistry {
    Map<ArchitectureCategory, Map<String, ArchitectureGenerator>> registry;

    public void register(String name, ArchitectureGenerator generator) {
        registry.putIfAbsent(generator.getCategory(), new HashMap<>());
        registry.get(generator.getCategory()).put(name, generator);
    }

    public Map<ArchitectureCategory, Map<String, ArchitectureGenerator>> getRegistry() {
        return registry;
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
