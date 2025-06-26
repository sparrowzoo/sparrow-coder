package com.sparrowzoo.coder.domain.bo.validate;

import com.sparrowzoo.coder.enums.DigitalCategory;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DigitalValidator implements Validator {
    private static DigitalValidator defaultValidator=defaultValidator();
    private Boolean i18n;
    private String propertyName;
    private String emptyMessage;
    private Boolean allowEmpty;
    private String digitalMessage;
    private Integer minValue;
    private String minValueMessage;
    private Integer maxValue;
    private String maxValueMessage;
    //INT FLOAT 科学计数法
    private DigitalCategory category;
    private Map<String, String> i18nConfig=new HashMap<>();

    public static DigitalValidator defaultValidator() {
        DigitalValidator validator=new DigitalValidator();
        validator.setEmptyMessage("不允许为空");
        validator.setMinValueMessage("不允许小于%s");
        validator.setMaxValueMessage("不允许大于%s");
        validator.setDigitalMessage("请输入正确的数字");
        return validator;
    }
}
