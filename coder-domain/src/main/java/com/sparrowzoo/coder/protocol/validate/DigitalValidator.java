package com.sparrow.coding.protocol.validate;

import com.sparrow.coding.DigitalCategory;
import com.sparrow.core.Pair;
import lombok.Data;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Data
public class DigitalValidator implements Validator {
    private Boolean i18n;
    private String i18nFieldName;
    private String emptyMessage;
    private Boolean allowEmpty;
    private String digitalMessage;
    private int minValue;
    private String minValueMessage;
    private int maxValue;
    private String maxValueMessage;
    //INT FLOAT 科学计数法
    private DigitalCategory category;
    private List<Pair<String, String>> i18nConfig=new ArrayList<>();
}
