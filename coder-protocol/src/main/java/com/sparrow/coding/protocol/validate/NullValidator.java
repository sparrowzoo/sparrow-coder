package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface NullValidator {
    String prompt() default "";

    String nullError() default "";

    boolean allowNull() default false;

    int minLength() default 1;

    int maxLength() default 20;

    String lengthError() default "";

    String defaultValue() default "";
}
