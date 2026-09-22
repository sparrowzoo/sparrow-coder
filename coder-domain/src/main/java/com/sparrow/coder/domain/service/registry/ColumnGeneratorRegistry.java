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
import com.sparrow.coder.domain.service.frontend.generator.column.ColumnGenerator;

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
        private static final ColumnGeneratorRegistry COLUMN_GENERATOR_REGISTRY = new ColumnGeneratorRegistry();
    }

    public static ColumnGeneratorRegistry getInstance() {
        return Inner.COLUMN_GENERATOR_REGISTRY;
    }


}
