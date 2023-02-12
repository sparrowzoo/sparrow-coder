package com.sparrow.coding.protocol.validate;

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
