package com.sparrow.coding.frontend.validate;

import com.sparrow.utility.StringUtility;

public class ValidatorMessageUtils {
    private static final String ERROR_PREFIX = "error";

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
     * @param controlPrefix
     * @return
     */
    public static String getFieldKey(String controlPrefix, String upperFieldName) {
        return String.format("%s:{", controlPrefix + upperFieldName);
    }

    /**
     * 对应 errorCtrlId : 'errorAppName',
     * @return
     */
    public static String getErrorCtrlId(String upperFieldName) {
        return String.format("errorCtrlId : '%s',", ERROR_PREFIX + upperFieldName);
    }

    /**
     * prompt : '请输入20位以下APP名称',
     *
     * @param key
     * @param message
     * @return
     */
    public static String formatMessage(String key, String message) {
        return String.format("%1$s : '%2$s',", key, message);
    }

    /**
     * minLength : 1,
     *
     * @param key
     * @param digital
     * @return
     */
    public static String formatMessage(String key, Integer digital) {
        return String.format("%1$s : %2$s,", key, digital);
    }

    public static String formatMessage(String key, Boolean digital) {
        return String.format("%1$s : %2$s,", key, digital);
    }
}
