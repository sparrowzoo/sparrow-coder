package com.sparrow.coding.frontend.validate.valibot;

import com.sparrow.coding.DigitalCategory;
import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.api.ValidatorRegistry;
import com.sparrow.coding.protocol.validate.DigitalValidator;
import com.sparrow.coding.protocol.validate.StringValidator;
import com.sparrow.coding.protocol.validate.Validator;
import com.sparrow.utility.StringUtility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractValidatorMessageGenerator<T> implements ValidatorMessageGenerator<T>, InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(AbstractValidatorMessageGenerator.class);
    private ValidatorRegistry registry = ValidatorRegistry.getInstance();

    public AbstractValidatorMessageGenerator() {
    }

    /**
     * 生成示例
     * <pre>
     * age: v.pipe(
     *         v.string(),// 初始字符串输入
     *         v.transform((input): number | string => {
     *             //是否为浮点数
     *             const match = /^-?\d+$/.test(input);
     *             if (match) {
     *                 return parseInt(input);
     *             }
     *             return input;
     *         }),
     *         v.number("请输入数字"), // 确保转换后为数字类型
     *         v.minValue(1, '年龄不能小于1岁'),
     *         v.maxValue(100, '年龄不能超过100岁'),
     *         v.nonEmpty("Please enter your password."),
     *         v.minLength(8, "Your password must have 8 characters or more.")
     *     )
     * </pre>
     * 对应AppName:{
     * * @return
     */
    protected String start(String propertyName) {
        return String.format("%s:{\n v.pipe(\n v.string(),", propertyName);
    }

    protected String getMessage(Boolean i18n, String i18nKey, String message) {
        if (i18n) {
            return String.format("translate(\"%s\")", i18nKey);
        }
        return message;
    }

    protected String nonEmpty(Validator validator) {
        return String.format("v.nonEmpty(%s),", this.getMessage(validator.getI18n(), "empty-message", validator.getEmptyMessage()));
    }

    protected String allowEmpty(String pipeline) {
        return " v.union([v.literal(\"\")," + pipeline + "])";
    }


    protected void finish(StringBuilder sb) {
        sb.deleteCharAt(sb.length() - 2);
        sb.append("}\n");
    }

    @Override
    public void afterPropertiesSet() {
        this.registry.registry(this);
    }

    protected String minLength(StringValidator validator) {
        return String.format("v.minLength(%1$s, %2$s),", validator.getMinLength(), this.getMessage(validator.getI18n(), "length-message", validator.getLengthMessage()));
    }

    protected String maxLength(StringValidator validator) {
        return String.format("v.maxLength(%1$s, %2$s),", validator.getMaxLength(), this.getMessage(validator.getI18n(), "length-message", validator.getLengthMessage()));
    }



    protected String number() {
        return "v.number(),";
    }

    protected String transform(DigitalCategory category) {
        return String.format("v.transform((input): number | string => {\n" +
                "            const match = %1$s.test(input);\n" +
                "            if (match) {\n" +
                "                return parseInt(input);\n" +
                "            }\n" +
                "            return input;\n" +
                "        }),", category.getRegex());
    }

    protected String minValue(DigitalValidator validator) {
        return String.format("v.minValue(%1$s, %2$s),", validator.getMinValue(), this.getMessage(validator.getI18n(), "value-message", validator.getDigitalMessage()));
    }

    protected String maxValue(DigitalValidator validator) {
        return String.format("v.maxValue(%1$s, %2$s),", validator.getMaxValue(), this.getMessage(validator.getI18n(), "value-message", validator.getDigitalMessage()));
    }

}
