package com.sparrowzoo.coder.service;

public interface ValidatorMessageGenerator<T> {
    String generateConfig(String propertyName,
                          T validator);

    String generateI18NConfig(T validator);
}
