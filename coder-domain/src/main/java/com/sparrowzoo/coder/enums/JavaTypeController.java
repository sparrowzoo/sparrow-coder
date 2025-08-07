package com.sparrowzoo.coder.enums;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public enum JavaTypeController {
    STRING(new ControlType[]{
            ControlType.INPUT_TEXT,
            ControlType.INPUT_PASSWORD,
            ControlType.DATE_HHMMSS,
            ControlType.EDITOR,
            ControlType.TEXT_AREA,
            ControlType.SELECT,
            ControlType.CODE,
            ControlType.FILE,
            ControlType.IMAGE,
            ControlType.INPUT_HIDDEN}),
    NUMBER(new ControlType[]{
            ControlType.INPUT_TEXT,
            ControlType.INPUT_HIDDEN,
            ControlType.SELECT}),
    BOOLEAN(new ControlType[]{
            ControlType.CHECK_BOX}),
    DATE(new ControlType[]{
            ControlType.DATE});

    private ControlType[] controlTypes;

    JavaTypeController(ControlType[] controlTypes) {
        this.controlTypes = controlTypes;
    }

    public static JavaTypeController getByJavaType(String className) {
        Class<?> javaType = null;
        try {
            javaType = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (javaType == String.class) {
            return STRING;
        }
        if (javaType == Byte.class ||
                javaType == Short.class ||
                javaType == Integer.class ||
                javaType == Long.class ||
                javaType == Double.class ||
                javaType == Float.class) {
            return NUMBER;
        }

        if (javaType == Boolean.class) {
            return BOOLEAN;
        }

        if (javaType == java.util.Date.class ||
                javaType == java.sql.Date.class ||
                javaType == LocalDate.class) {
            return DATE;
        }
        return STRING;
    }
}
