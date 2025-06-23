package com.sparrowzoo.coder.domain.bo.validate;

import lombok.Data;

import java.util.Map;

@Data
public class StringValidator implements Validator {
    public static StringValidator defaultValidator=StringValidator.defaultValidator();
    private Boolean i18n;
    private String propertyName;
    private String emptyMessage;
    private Boolean allowEmpty;
    protected int minLength;
    protected int maxLength;
    private String minLengthMessage;
    private String maxLengthMessage;
    private Map<String, String> i18nConfig;
    private static StringValidator defaultValidator() {
        StringValidator validator = new StringValidator();
        validator.setEmptyMessage("不允许为空");
        validator.setMinLengthMessage("至少%s个字符");
        validator.setMaxLengthMessage("最多不允许超过%s个字符");
        return validator;
    }
}
