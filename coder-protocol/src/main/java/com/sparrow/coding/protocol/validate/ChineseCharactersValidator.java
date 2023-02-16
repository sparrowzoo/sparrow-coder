package com.sparrow.coding.protocol.validate;

public @interface ChineseCharactersValidator {

    String prompt() default "请输入汉字";

    String nullError() default "";

    String chineseCharactersError() default "请输入汉字";

    int minLength() default 1;

    int maxLength() default 100;

    String lengthError() default "请输入1~100个汉字";

    String methodName() default "isChineseCharacters";

}
