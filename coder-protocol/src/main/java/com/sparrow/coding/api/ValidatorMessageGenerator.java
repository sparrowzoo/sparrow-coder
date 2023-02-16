package com.sparrow.coding.api;

import java.lang.annotation.Annotation;

public interface ValidatorMessageGenerator<T extends Annotation> {
    Class<T> getValidateAnnotation();

    String generateValidateMessage(String fieldName, String controlPrefix,
        Annotation validator) throws NoSuchFieldException, IllegalAccessException;
}
