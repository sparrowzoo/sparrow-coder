package com.sparrowzoo.coder.domain.bo.validate;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
@AllArgsConstructor
public class NoneValidator implements Validator {

    private String clazz;

    @Override
    public void setI18n(Boolean i18n) {

    }

    @Override
    public Boolean getI18n() {
        return null;
    }

    @Override
    public Boolean getAllowEmpty() {
        return true;
    }

    @Override
    public String getEmptyMessage() {
        return "";
    }

    @Override
    public Map<String, String> getI18nConfig() {
        return Collections.emptyMap();
    }

    @Override
    public void setI18nConfig(Map<String, String> i18nConfig) {

    }

}
