package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface IdCardValidator {

    String prompt() default "请输入身份证号码";

    String nullError() default "身份证号码不允许为空";

    String idCardError() default "身份证号码输入有误";

    boolean allowNull() default false;

    int minLength() default 18;

    int maxLength() default 18;

    String lengthError() default "请输入18位身份证号码";

    int parentLevel() default 1;
}
