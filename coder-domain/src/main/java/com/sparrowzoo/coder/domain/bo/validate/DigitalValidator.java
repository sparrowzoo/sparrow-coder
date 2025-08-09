package com.sparrowzoo.coder.domain.bo.validate;

import com.sparrowzoo.coder.enums.DigitalCategory;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DigitalValidator implements Validator, Cloneable<DigitalValidator> {
    public static DigitalValidator DIGITAL_VALIDATOR = new DigitalValidator();

    public static final String NAME = "digital";

    static {
        DIGITAL_VALIDATOR.setAllowEmpty(false);
        DIGITAL_VALIDATOR.setCategory(DigitalCategory.INTEGER);
        DIGITAL_VALIDATOR.setEmptyMessage("不允许为空");
        DIGITAL_VALIDATOR.setMinValueMessage("不允许小于%s");
        DIGITAL_VALIDATOR.setMaxValueMessage("不允许大于%s");
        DIGITAL_VALIDATOR.setDigitalMessage("请输入正确的数字");
    }

    private Boolean i18n;
    private String emptyMessage;
    private Boolean allowEmpty;
    private String digitalMessage;
    private Integer minValue;
    private String minValueMessage;
    private Integer maxValue;
    private String maxValueMessage;
    //INT FLOAT 科学计数法
    private DigitalCategory category;
    private Map<String, String> i18nConfig = new HashMap<>();

    public DigitalValidator create() {
        DigitalValidator validator = new DigitalValidator();
        validator.setI18n(DIGITAL_VALIDATOR.i18n);
        validator.setEmptyMessage(DIGITAL_VALIDATOR.emptyMessage);
        validator.setAllowEmpty(DIGITAL_VALIDATOR.allowEmpty);
        validator.setDigitalMessage(DIGITAL_VALIDATOR.digitalMessage);
        validator.setMinValue(DIGITAL_VALIDATOR.minValue);
        validator.setMinValueMessage(DIGITAL_VALIDATOR.minValueMessage);
        validator.setMaxValue(DIGITAL_VALIDATOR.maxValue);
        validator.setMaxValueMessage(DIGITAL_VALIDATOR.maxValueMessage);
        validator.setCategory(DIGITAL_VALIDATOR.category);
        validator.setI18nConfig(DIGITAL_VALIDATOR.i18nConfig);
        return validator;
    }
}
