package com.sparrowzoo.coder.domain.bo.validate;

import lombok.Data;

@Data
public class RegexValidator extends StringValidator {
    private String formatMessage;
    private String regex;
}
