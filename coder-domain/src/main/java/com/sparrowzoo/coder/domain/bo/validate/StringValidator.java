package com.sparrowzoo.coder.domain.bo.validate;

import com.sparrow.core.Pair;
import lombok.Data;

import java.util.List;

@Data
public class StringValidator implements Validator {
    private Boolean i18n;
    private String i18nFieldName;
    private String emptyMessage;
    private Boolean allowEmpty;
    protected int minLength;
    protected int maxLength;
    private String minLengthMessage;
    private String maxLengthMessage;
    private List<Pair<String, String>> i18nConfig;

}
