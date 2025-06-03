package com.sparrow.coding.protocol.validate;

import com.sparrow.coding.DigitalCategory;
import lombok.Data;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Data
public class DigitalValidator implements Validator {
    private Boolean i18n;
    private String emptyMessage;
    private Boolean allowEmpty;
    private String digitalMessage;
    private int minValue;
    private int maxValue;
    //INT FLOAT 科学计数法
    private DigitalCategory category;
}
