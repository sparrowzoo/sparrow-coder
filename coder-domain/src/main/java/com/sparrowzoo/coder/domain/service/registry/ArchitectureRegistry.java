package com.sparrowzoo.coder.domain.service.registry;

import com.alibaba.fastjson.JSON;
import com.sparrow.core.spi.JsonFactory;
import com.sparrow.json.Json;
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
        return registry.get(category).get(name);
    }

    public Map<ArchitectureCategory, ArchitectureGenerator> parse(String config) {
        Map<ArchitectureCategory, ArchitectureGenerator> map = new HashMap<>();
        Map<String, String> configPairs = JsonFactory.getProvider().parse(config, Map.class);
        for (String category : configPairs.keySet()) {
            ArchitectureCategory categoryEnum = ArchitectureCategory.valueOf(category);
            String architecture = configPairs.get(category);
            ArchitectureGenerator generator = this.getGenerator(categoryEnum, architecture);
            map.put(categoryEnum, generator);
        }
        return map;
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
