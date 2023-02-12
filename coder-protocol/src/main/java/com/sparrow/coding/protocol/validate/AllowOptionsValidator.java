package com.sparrow.coding.protocol.validate;

public @interface AllowOptionsValidator {

    String[] options() default {};

    String defaultValue() default "";
}
