package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface AllowInputCharLengthValidator {
    String prompt() default "";

    int maxAllowCharLength() default 500;

    String allowCharLengthShowControlId() default "spanAllowCharLength";

    String maxCharLengthControlId() default "spanMaxCharLength";

    boolean allowNull() default true;
}
