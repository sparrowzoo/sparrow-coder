package com.sparrow.coding.api;

import java.lang.annotation.Annotation;

public interface ValidatorMessageGenerator<T extends Annotation> {
    String generateValidateMessage(String fieldName, String controlPrefix, T validator) throws NoSuchFieldException, IllegalAccessException;
}
