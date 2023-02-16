package com.sparrow.coding.protocol.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface DigitalValidator {

    String prompt() default "请输入数字(支持小数)";

    String nullError() default "不允许为空";

    boolean allowNull() default false;

    String digitalError() default "请输入数字（支持小数)";

    int minValue() default 0;

    int maxValue() default 999;

    //如何是int 的最小值，说明用户未配置默认值
    int defaultValue() default Integer.MIN_VALUE;

    String methodName() default "isDigital";
}
