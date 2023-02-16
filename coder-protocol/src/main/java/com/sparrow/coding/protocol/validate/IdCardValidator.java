package com.sparrow.coding.protocol.validate;

public @interface IdCardValidator {

    String prompt() default "请输入身份证号码";

    String nullError() default "身份证号码不允许为空";

    String idCardError() default "身份证号码输入有误";

    boolean allowNull() default false;

    int minLength() default 18;

    int maxLength() default 18;

    String lengthError() default "请输入18位身份证号码";

    int parentLevel() default 1;

    String methodName() default "isIdCard";

}
