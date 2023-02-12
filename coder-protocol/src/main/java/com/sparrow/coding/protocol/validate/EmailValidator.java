package com.sparrow.coding.protocol.validate;

public @interface EmailValidator {

    String prompt() default "输入您的E-MAIL,方便您日后找回密码。没有电子邮箱?推荐使用<a target=\"_blank\" unselectable=\"on\" style=\"color:#ff9900;\" href=\"http://reg.email.163.com/mailregAll/reg0.jsp?from='+$.url.root+'\">网易</a>邮箱。'";

    String nullError() default "请输入您注册的电子邮箱。";

    boolean allowNull() default false;

    String emailError() default "电子邮箱格式输入有误";

    int minLength() default 6;

    int maxLength() default 200;

    String lengthError() default "电子邮件长度在6-200位之间";

    String setError() default "电子邮箱已存在,请换用其它电子邮件。没有电子邮箱?推荐使用<a target=\"_blank\" unselectable=\"on\" href=\"http://reg.email.163.com/mailregAll/reg0.jsp?from='+$.url.root+'\">网易</a>邮箱。'";

    int parentLevel() default 1;
}
