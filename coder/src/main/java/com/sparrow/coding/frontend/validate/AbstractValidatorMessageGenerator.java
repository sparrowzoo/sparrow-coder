package com.sparrow.coding.frontend.validate;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.utility.StringUtility;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

public class AbstractValidatorMessageGenerator<T extends Annotation> implements ValidatorMessageGenerator<T> {
    private static final String ERROR_PREFIX = "error";

    private static final String DEFAULT_VALUE = "defaultValue";

    /**
     * 生成示例
     * <pre>
     * txtAppName : {
     * 		errorCtrlId : 'errorAppName',
     * 		prompt : '请输入20位以下APP名称',
     * 		nullError : 'APP名称为必添项。',
     * 		minLength : 1,
     * 		maxLength : 20,
     * 		lengthError : 'APP名称长度不得多于20。'
     * }
     * </pre>
     * 对应txtAppName:{
     *
     * @param controlPrefix
     * @return
     */
    private String getFieldKey(String controlPrefix, String upperFieldName) {
        return String.format("%s:{\n", controlPrefix + upperFieldName);
    }

    /**
     * 对应 errorCtrlId : 'errorAppName',
     *
     * @return
     */
    private String getErrorCtrlId(String upperFieldName) {
        return String.format("\"errorCtrlId\" : '%s',\n", ERROR_PREFIX + upperFieldName);
    }

    /**
     * prompt : '请输入20位以下APP名称',
     *
     * @param key
     * @param message
     * @return
     */
    private String formatMessage(String key, Object message) {
        if (message instanceof String) {
            return String.format("\"%1$s\" : '%2$s',\n", key, message);
        }
        return String.format("\"%1$s\" : %2$s,\n", key, message);
    }

    private void finish(StringBuilder sb) {
        sb.deleteCharAt(sb.length() - 2);
        sb.append("}\n");
    }

    protected void appendDefaultValue(StringBuilder sb, Map<String, Object> maps) {
        Object defaultValue = maps.get(DEFAULT_VALUE);
        if (defaultValue != null) {
            sb.append(this.formatMessage("defaultValue", defaultValue));
        }
    }

    @Override public String generateValidateMessage(String fieldName, String controlPrefix,
        T validator) throws NoSuchFieldException, IllegalAccessException {
        String upperFieldName = StringUtility.setFirstByteUpperCase(fieldName);
        String validatorKey = this.getFieldKey(controlPrefix, upperFieldName);
        String errorCtrlId = this.getErrorCtrlId(upperFieldName);
        StringBuilder sb = new StringBuilder();
        sb.append(validatorKey);
        sb.append(errorCtrlId);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(validator);
        Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
        memberValues.setAccessible(true);
        Map<String, Object> maps = (Map<String, Object>) memberValues.get(invocationHandler);
        for (String key : maps.keySet()) {
            sb.append(this.formatMessage(key, maps.get(key)));
        }
        this.appendDefaultValue(sb, maps);
        this.finish(sb);
        return sb.toString();
    }
}
