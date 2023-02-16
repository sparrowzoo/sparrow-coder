package com.sparrow.coding.protocol.validate;

public @interface TelValidator {
    String prompt() default "请输入电话号码";

    String nullError() default "电话号码为必填项。";

    boolean allowNull() default false;

    String telError() default "电话号码输入有误 例：010-88888888";

    int minLength() default 7;

    int maxLength() default 12;

    String lengthError() default "电话号码长度在7-12位之间";

    int parentLevel() default 1;

    String methodName() default "isTel";

}
