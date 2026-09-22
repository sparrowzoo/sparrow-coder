/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.coder.domain.service.registry;

import com.sparrow.coder.domain.service.ArchitectureGenerator;
import com.sparrow.coder.enums.ArchitectureCategory;

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
        if (architectureMap == null) {
            return null;
        }
        if (architectureMap.containsKey(name)) {
            return architectureMap.get(name);
        }
        return architectureMap.values().iterator().next();
    }

    private ArchitectureRegistry() {
        registry = new HashMap<>();
    }


    static class Inner {
        private static final ArchitectureRegistry ARCHITECTURE_REGISTRY = new ArchitectureRegistry();
    }

    public static ArchitectureRegistry getInstance() {
        return ArchitectureRegistry.Inner.ARCHITECTURE_REGISTRY;
    }

}
