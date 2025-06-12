package com.sparrow.coding.api;

public interface ValidatorMessageGenerator<T> {
    String generateConfig(String propertyName,
                          T validator);

    String generateI18NConfig(T validator);
}
