package com.sparrow.coding.api;

import com.sparrow.coding.protocol.validate.Validator;
import com.sparrow.utility.ClassUtility;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ValidatorRegistry {

    private Map<String, Map<String, ValidatorMessageGenerator<?>>> registry;

    private ValidatorRegistry() {
        registry = new HashMap<>();
    }

    static class Inner {
        private static ValidatorRegistry validatorRegistry = new ValidatorRegistry();
    }


    public void registry(ValidatorMessageGenerator<?> validatorMessageGenerator) {
        String packageName = validatorMessageGenerator.getClass().getPackage().getName();
        String namespace = packageName.substring(packageName.lastIndexOf(".") + 1);
        String validatorName = ClassUtility.getEntityNameByClass(validatorMessageGenerator.getClass());
        if (this.registry.containsKey(namespace)) {
            this.registry.putIfAbsent(namespace, new HashMap<>());
        }
        this.registry.get(namespace).put(validatorName, validatorMessageGenerator);
    }

    public ValidatorMessageGenerator<?> getValidatorMessageGenerator(String namespace,
                                                                             Class<? extends Annotation> clazz) {
        String validatorName = ClassUtility.getEntityNameByClass(clazz);
        return this.registry.get(namespace).get(validatorName);
    }

    public Map<String, Map<String, ValidatorMessageGenerator<?>>> getRegistry() {
        return registry;
    }

    public static ValidatorRegistry getInstance() {
        return Inner.validatorRegistry;
    }
}
