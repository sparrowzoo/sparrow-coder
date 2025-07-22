package com.sparrowzoo.coder.domain.service;

import com.sparrowzoo.coder.domain.bo.validate.Validator;

public interface ValidatorMessageGenerator<T extends Validator> {
    String generateConfig(String propertyName,
                          T validator);

     T defaultValidator();
}
