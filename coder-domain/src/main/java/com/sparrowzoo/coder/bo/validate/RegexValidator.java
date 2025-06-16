package com.sparrowzoo.coder.bo.validate;

import lombok.Data;

@Data
public class RegexValidator extends StringValidator {
    private String formatMessage;
    private String regex;
}
