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

import com.sparrow.container.FactoryBean;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.PlaceholderExtension;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class PlaceholderExtensionRegistry implements FactoryBean<PlaceholderExtension> {


    private PlaceholderExtensionRegistry() {
    }

    static class Inner {
        private static final PlaceholderExtensionRegistry PLACEHOLDER_EXTENSION_REGISTRY = new PlaceholderExtensionRegistry();
    }

    public static PlaceholderExtensionRegistry getInstance() {
        return Inner.PLACEHOLDER_EXTENSION_REGISTRY;
    }

    private Map<String, PlaceholderExtension> dependencyPlaceholderExtensionMap = new HashMap<>();


    private Map<String, PlaceholderExtension> placeholderExtensionMap = new HashMap<>();

    @Override
    public void pubObject(String s, PlaceholderExtension placeholderExtension) {
        if (placeholderExtension.getClass().getName().contains("dependency")) {
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
        for (String name : this.dependencyPlaceholderExtensionMap.keySet()) {
            PlaceholderExtension extension = this.dependencyPlaceholderExtensionMap.get(name);
            if (extension == null) {
                log.error("placeholder extension not found: " + name);
                continue;
            }
            extension.extend(tableContext, registry);
        }
    }
}
