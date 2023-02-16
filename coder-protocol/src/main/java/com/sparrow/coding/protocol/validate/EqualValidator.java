package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface EqualValidator {

    String prompt() default "";

    String otherCtrlId() default "";

    String nullError() default "";

    String noEqualError() default "";

    int minLength() default 1;

    int maxLength() default 1;

    String lengthError() default "";

    int parentLevel() default 1;
}
