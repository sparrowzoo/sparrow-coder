package com.sparrowzoo.coder.domain.bo.validate;

import lombok.Data;

import java.util.Map;

@Data
public class StringValidator implements Validator, Cloneable<StringValidator> {
    public static final String NAME="string";

    public static final StringValidator STRING_VALIDATOR = new StringValidator();
    static {
        STRING_VALIDATOR.setEmptyMessage("不允许为空");
        STRING_VALIDATOR.setMinLengthMessage("至少%s个字符");
        STRING_VALIDATOR.setMaxLengthMessage("最多不允许超过%s个字符");
        STRING_VALIDATOR.setAllowEmpty(false);
        STRING_VALIDATOR.setI18n(true);
        STRING_VALIDATOR.setMinLength(null);
        STRING_VALIDATOR.setMaxLength(null);
    }



    public StringValidator defaultValidator() {
        return STRING_VALIDATOR;
    }

    protected Boolean i18n;
    protected String emptyMessage;
    protected Boolean allowEmpty;
    protected Integer minLength;
    protected Integer maxLength;
    protected String minLengthMessage;
    protected String maxLengthMessage;
    protected Map<String, String> i18nConfig;


    @Override
    public StringValidator create() {
        StringValidator validator =new StringValidator();
        validator.setI18n(STRING_VALIDATOR.getI18n());
        validator.setEmptyMessage(STRING_VALIDATOR.emptyMessage);
        validator.setAllowEmpty(STRING_VALIDATOR.allowEmpty);
        validator.setMinLength(STRING_VALIDATOR.minLength);
        validator.setMaxLength(STRING_VALIDATOR.maxLength);
        validator.setMinLengthMessage(STRING_VALIDATOR.minLengthMessage);
        validator.setMaxLengthMessage(STRING_VALIDATOR.maxLengthMessage);
        validator.setI18nConfig(STRING_VALIDATOR.i18nConfig);
        return validator;
    }
}
