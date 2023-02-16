package com.sparrow.coding.protocol.validate;

public @interface AllowInputCharLengthValidator {
    String prompt() default "";

    int maxAllowCharLength() default 500;

    String allowCharLengthShowControlId() default "spanAllowCharLength";

    boolean allowNull() default true;

}
