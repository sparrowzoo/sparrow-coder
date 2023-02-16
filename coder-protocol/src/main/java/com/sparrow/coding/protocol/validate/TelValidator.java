package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface TelValidator {
    String prompt() default "请输入电话号码";

    String nullError() default "电话号码为必填项。";

    boolean allowNull() default false;

    String telError() default "电话号码输入有误 例：010-88888888";

    int minLength() default 7;

    int maxLength() default 12;

    String lengthError() default "电话号码长度在7-12位之间";

    int parentLevel() default 1;
}
