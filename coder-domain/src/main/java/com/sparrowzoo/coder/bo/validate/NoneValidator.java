package com.sparrowzoo.coder.bo.validate;

import com.sparrow.core.Pair;

import java.util.Collections;
import java.util.List;

public class NoneValidator implements Validator {

    @Override
    public Boolean getI18n() {
        return null;
    }

    @Override
    public void setI18nFieldName(String i18nFieldName) {

    }

    @Override
    public String getI18nFieldName() {
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
    public List<Pair<String, String>> getI18nConfig() {
        return Collections.emptyList();
    }

}
