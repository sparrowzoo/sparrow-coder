package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * KB OR MB
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ImageLengthValidator {
    String defaultValue() default "";
}
