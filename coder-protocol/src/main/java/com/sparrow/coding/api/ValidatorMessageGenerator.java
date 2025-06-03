package com.sparrow.coding.api;

public interface ValidatorMessageGenerator<T> {
    String generateValidateMessage(String propertyName,
        T validator);
}
