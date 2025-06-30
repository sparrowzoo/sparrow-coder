package com.sparrowzoo.coder.domain.bo.validate;

import lombok.Data;

import java.util.Collections;
import java.util.Map;

@Data
public class NoneValidator implements Validator {

    public NoneValidator(String clazz) {
        this.clazz = clazz;
    }

    private String clazz;

    @Override
    public void setI18n(Boolean i18n) {

    }

    @Override
    public Boolean getI18n() {
        return null;
    }

    @Override
    public void setPropertyName(String i18nFieldName) {

    }

    @Override
    public String getPropertyName() {
        return "";
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
