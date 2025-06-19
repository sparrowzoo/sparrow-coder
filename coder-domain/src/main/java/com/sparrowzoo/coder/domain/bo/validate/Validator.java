package com.sparrowzoo.coder.domain.bo.validate;

import com.sparrow.core.Pair;

import java.util.List;

public interface Validator {
    Boolean getI18n();

    void setI18nFieldName(String i18nFieldName);

    String getI18nFieldName();

    Boolean getAllowEmpty();

    String getEmptyMessage();

    //国际化message.tsx 文件内容
    List<Pair<String, String>> getI18nConfig();
}
