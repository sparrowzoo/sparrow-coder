package com.sparrow.coder.domain.service;

import com.sparrow.coder.domain.bo.validate.Validator;

public interface ValidatorMessageGenerator<T extends Validator> {
    String generateConfig(String propertyName,
                          T validator);

     T defaultValidator();
}
