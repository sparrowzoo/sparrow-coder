package com.sparrowzoo.coder.domain.service;

public interface ValidatorMessageGenerator<T> {
    String generateConfig(String propertyName,
                          T validator);

     T defaultValidator();
}
