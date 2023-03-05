package com.sparrow.coding.frontend;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ValidatorRegistry {

    private Map<Class<? extends Annotation>, ValidatorMessageGenerator<? extends Annotation>> registry;

    private ValidatorRegistry() {
        registry = new HashMap<>();
    }

    static class Inner {
        private static ValidatorRegistry validatorRegistry = new ValidatorRegistry();
    }

    public void registry(ValidatorMessageGenerator<? extends Annotation> validatorMessageGenerator) {
        this.registry.put(validatorMessageGenerator.getValidateAnnotation(), validatorMessageGenerator);
    }

    public ValidatorMessageGenerator<? extends Annotation> getValidatorMessageGenerator(
        Class<? extends Annotation> clazz) {
        return this.registry.get(clazz);
    }

    public Map<Class<? extends Annotation>, ValidatorMessageGenerator<? extends Annotation>> getRegistry() {
        return registry;
    }

    public static ValidatorRegistry getInstance() {
        return Inner.validatorRegistry;
    }
}
