package com.sparrowzoo.coder.bo.validate;

import com.sparrow.core.Pair;
import com.sparrowzoo.coder.enums.DigitalCategory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
