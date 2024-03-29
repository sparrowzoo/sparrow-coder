package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface MobileValidator {
    String prompt() default "请输入手机号码";

    String nullError() default "手机号码不允许为空";

    String mobileError() default "手机号码输入有误 例：13588888888";

    boolean allowNull() default false;

    int minLength() default 11;

    int maxLength() default 11;

    String lengthError() default "请输入11位手机号码";

    int parentLevel() default 1;
}
