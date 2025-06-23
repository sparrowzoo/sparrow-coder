package com.sparrowzoo.coder.domain.bo.validate;

import java.util.Map;

public interface Validator {
    void setI18n(Boolean i18n);
    Boolean getI18n();

    void setPropertyName(String propertyName);

    String getPropertyName();

    Boolean getAllowEmpty();

    String getEmptyMessage();

    //国际化message.tsx 文件内容
    Map<String, String> getI18nConfig();

    void setI18nConfig(Map<String, String> i18nConfig);
}
