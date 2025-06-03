package com.sparrow.coding.protocol.validate;

import lombok.Data;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Data
public class EqualValidator extends RegexValidator {
    private String compareProperty;
}
