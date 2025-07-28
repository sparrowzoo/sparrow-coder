package com.sparrowzoo.coder.utils;

public class JavaTsTypeConverter {
    public static String toTsType(Class<?> clazz) {
        if (Number.class.isAssignableFrom(clazz)) {
            return "number";
        }
        if (Boolean.class.isAssignableFrom(clazz)) {
            return "boolean";
        }
        return "string";
    }
}
