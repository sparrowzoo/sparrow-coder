package com.sparrowzoo.coder.service.registry;

import com.sparrowzoo.coder.enums.ArchitectureCategory;
import com.sparrowzoo.coder.service.ArchitectureGenerator;

import java.util.HashMap;
import java.util.Map;

public class ArchitectureRegistry {
    Map<ArchitectureCategory, Map<String, ArchitectureGenerator>> registry = new HashMap<>();

    public void register(String name, ArchitectureGenerator generator) {
        registry.putIfAbsent(generator.getCategory(), new HashMap<>());
        registry.get(generator.getCategory()).put(name, generator);
    }

    public Map<ArchitectureCategory, Map<String, ArchitectureGenerator>> getRegistry() {
        return registry;
    }
}
