package com.sparrowzoo.coder.domain.service.registry;

import com.sparrow.protocol.KeyValue;
import com.sparrow.utility.ClassUtility;
import com.sparrowzoo.coder.domain.service.ValidatorMessageGenerator;

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
