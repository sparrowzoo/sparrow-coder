package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ChineseCharactersValidator {

    String prompt() default "请输入汉字";

    String nullError() default "";

    String chineseCharactersError() default "请输入汉字";

    int minLength() default 1;

    int maxLength() default 100;

    String lengthError() default "请输入1~100个汉字";
}
