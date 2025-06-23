package com.sparrowzoo.coder.domain.bo.validate;

import lombok.Data;

@Data
public class RegexValidator extends StringValidator {
    public static RegexValidator defaultValidator=RegexValidator.defaultValidator();
    private String formatMessage;
    private String regex;

    public static RegexValidator defaultValidator() {
        StringValidator validator= StringValidator.defaultValidator;
        RegexValidator regexValidator=new RegexValidator();
        regexValidator.setEmptyMessage(validator.getEmptyMessage());
        regexValidator.setMinLengthMessage(validator.getMinLengthMessage());
        regexValidator.setMaxLengthMessage(validator.getMaxLengthMessage());
        regexValidator.setFormatMessage("请输入正确格式的：%s");
        return regexValidator;
    }
}
