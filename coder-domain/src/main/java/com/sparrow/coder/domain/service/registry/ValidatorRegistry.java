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

import com.sparrow.protocol.KeyValue;
import com.sparrow.utility.ClassUtility;
import com.sparrow.coder.domain.service.ValidatorMessageGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidatorRegistry {

    /**
     * key: namespace
     * value: Map<validatorName, ValidatorMessageGenerator>
     */
    private Map<String, Map<String, ValidatorMessageGenerator>> registry;

    private ValidatorRegistry() {
        registry = new HashMap<>();
    }

    static class Inner {
        private static ValidatorRegistry validatorRegistry = new ValidatorRegistry();
    }


    public void registry(ValidatorMessageGenerator<?> validatorMessageGenerator) {
        String packageName = validatorMessageGenerator.getClass().getPackage().getName();
        String namespace = packageName.substring(packageName.lastIndexOf(".") + 1);
        String validatorName = ClassUtility.getBeanNameByClass(validatorMessageGenerator.getClass(), ValidatorMessageGenerator.class);
        if (!this.registry.containsKey(namespace)) {
            this.registry.putIfAbsent(namespace, new HashMap<>());
        }
        this.registry.get(namespace).put(validatorName, validatorMessageGenerator);
    }

    public ValidatorMessageGenerator<?> getValidatorMessageGenerator(String namespace,
                                                                     String validatorName) {
        return this.registry.get(namespace).get(validatorName);
    }

    public Map<String, Map<String, ValidatorMessageGenerator>> getRegistry() {
        return registry;
    }

    public static ValidatorRegistry getInstance() {
        return Inner.validatorRegistry;
    }

    public List<KeyValue<String, String>> getValidatorNames(String namespace) {
        Map<String, ValidatorMessageGenerator> validators = this.registry.get(namespace);
        List<KeyValue<String, String>> list = new ArrayList<>();
        for (String validatorName : validators.keySet()) {
            list.add(new KeyValue<>(validatorName, validatorName));
        }
        return list;
    }
}
