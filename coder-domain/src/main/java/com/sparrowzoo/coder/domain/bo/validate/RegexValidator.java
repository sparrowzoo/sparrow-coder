package com.sparrowzoo.coder.domain.bo.validate;

import lombok.Data;

@Data
public class RegexValidator extends StringValidator implements Validator {
    public static RegexValidator REGEX_VALIDATOR=new RegexValidator();
    static {
        StringValidator stringValidator=StringValidator.STRING_VALIDATOR.create();
        REGEX_VALIDATOR.setI18n(stringValidator.i18n);
        REGEX_VALIDATOR.setEmptyMessage(stringValidator.emptyMessage);
        REGEX_VALIDATOR.setAllowEmpty(stringValidator.allowEmpty);
        REGEX_VALIDATOR.setMinLength(stringValidator.minLength);
        REGEX_VALIDATOR.setMaxLength(stringValidator.maxLength);
        REGEX_VALIDATOR.setMinLengthMessage(stringValidator.minLengthMessage);
        REGEX_VALIDATOR.setMaxLengthMessage(stringValidator.maxLengthMessage);
        REGEX_VALIDATOR.setI18nConfig(stringValidator.i18nConfig);
        REGEX_VALIDATOR.setFormatMessage("请输入正确格式的：%s");
    }
    private String formatMessage;
    private String regex;

    public RegexValidator create() {
        RegexValidator validator=new RegexValidator();
        validator.setFormatMessage(REGEX_VALIDATOR.formatMessage);
        validator.setRegex(REGEX_VALIDATOR.regex);
        validator.setI18n(REGEX_VALIDATOR.i18n);
        validator.setEmptyMessage(REGEX_VALIDATOR.emptyMessage);
        validator.setAllowEmpty(REGEX_VALIDATOR.allowEmpty);
        validator.setMinLength(REGEX_VALIDATOR.minLength);
        validator.setMaxLength(REGEX_VALIDATOR.maxLength);
        validator.setMinLengthMessage(REGEX_VALIDATOR.minLengthMessage);
        validator.setMaxLengthMessage(REGEX_VALIDATOR.maxLengthMessage);
        validator.setI18nConfig(REGEX_VALIDATOR.i18nConfig);
        return validator;
    }
}
