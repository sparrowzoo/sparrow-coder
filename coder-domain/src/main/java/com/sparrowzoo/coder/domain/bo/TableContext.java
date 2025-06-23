package com.sparrowzoo.coder.domain.bo;

import com.sparrow.orm.EntityManager;
import com.sparrow.protocol.constant.SparrowError;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TableContext {
    private Map<String, String> placeHolder;
    private TableConfigBO tableConfig;
    private EntityManager entityManager;
    private Map<String, Object> i18nMap = new HashMap<>();

    public TableContext(TableConfigBO tableConfig) {
        this.tableConfig = tableConfig;
        this.initErrorMessage();
    }

    public void initErrorMessage() {
        i18nMap.putIfAbsent("ErrorMessage", new HashMap<>());
        Map<String, String> errorMessages = (Map<String, String>) i18nMap.get("ErrorMessage");
        errorMessages.put(SparrowError.SYSTEM_SERVER_ERROR.name().toLowerCase(), "系统错误，请稍侯再试...");
        errorMessages.put(SparrowError.GLOBAL_PARAMETER_NULL.name().toLowerCase(), "参数不能为空...");
    }

    public Map<String, String> getValidateI18nMap(String propertyName) {
        i18nMap.putIfAbsent("validate", new HashMap<>());
        Map<String, Object> i18nMessages = (Map<String, Object>) i18nMap.get("validate");
        i18nMessages.putIfAbsent(propertyName, new HashMap<>());
        return (Map<String, String>) i18nMessages.get(propertyName);
    }

    public String getPoPackage() {
        String className = tableConfig.getClassName();
        return className.substring(0, className.lastIndexOf("."));
    }
}
