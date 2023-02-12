package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface UserNameRuleValidator {
    String prompt() default "请输入6-20个字符(字母、数字或下划线)推荐字母+数字组合的用户名。";

    String nullError() default "请输入您注册的用户名.";

    int minLength() default 6;

    int maxLength() default 20;

    String nameRuleError() default "请输入6-20个字符(字母、数字或下划线)推荐字母+数字组合的用户名。";

    String lengthError() default "用户名至少6位";

    String setError() default "用户名已被注册,请您换一个用户名。";

    int parentLevel() default 1;
}
