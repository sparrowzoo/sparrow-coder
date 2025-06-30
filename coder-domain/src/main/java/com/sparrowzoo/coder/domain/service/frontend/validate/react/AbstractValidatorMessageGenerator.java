package com.sparrowzoo.coder.domain.service.frontend.validate.react;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.core.Pair;
import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.validate.DigitalValidator;
import com.sparrowzoo.coder.domain.bo.validate.StringValidator;
import com.sparrowzoo.coder.domain.bo.validate.Validator;
import com.sparrowzoo.coder.enums.DigitalCategory;
import com.sparrowzoo.coder.domain.service.ValidatorMessageGenerator;
import com.sparrowzoo.coder.domain.service.registry.ValidatorRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

@Slf4j
public abstract class AbstractValidatorMessageGenerator<T extends Validator> implements ValidatorMessageGenerator<T>, InitializingBean, ContainerAware {
    private ValidatorRegistry registry = ValidatorRegistry.getInstance();

    protected T defaultValidator=this.defaultValidator();

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
    protected String pipeline() {
        return "v.pipe(\n v.string()";
    }

    protected String getMessage(Validator validator, String i18nKey, String message) {
        if (validator.getI18n()) {
            Map<String, String> configList = validator.getI18nConfig();
            configList.put(i18nKey, message);
            return String.format("translate(\"%1$s.%2$s\")", validator.getPropertyName(), i18nKey);
        }
        return String.format("\"%s\"", message);
    }

    protected String nonEmpty(T validator) {
        String message = validator.getEmptyMessage();
        if(StringUtility.isNullOrEmpty(message)){
            message=StringValidator.defaultValidator.getEmptyMessage();
        }
        if (!validator.getAllowEmpty()) {
            return String.format(",\nv.nonEmpty(%s)", this.getMessage(validator, "empty-message", message));
        }
        return "";
    }

    protected String allowEmpty(String pipeline) {
        return String.format("v.union([v.literal(\"\"),%s], (issue) => {" +
                "        if (issue.issues) {\n" +
                "            return issue.issues[issue.issues.length - 1].message;\n" +
                "        }\n" +
                "        return \"\";\n" +
                "    })", pipeline);
    }


    protected void finish(StringBuilder sb) {
        sb.append(")\n");
    }

    @Override
    public void afterPropertiesSet() {
        this.registry.registry(this);
    }

    @Override
    public void aware(Container container, String s) {
        this.registry.registry(this);
    }

    protected String minLength(StringValidator validator) {
        if(validator.getMinLength()==null){
            return "";
        }
        String message = validator.getMinLengthMessage();
        if(StringUtility.isNullOrEmpty(message)){
            message=String.format(StringValidator.defaultValidator.getMinLengthMessage(), validator.getMinLength());
        }
        return String.format(",\n v.minLength(%1$s, %2$s)", validator.getMinLength(), this.getMessage(validator, "min-length-message", message));
    }

    protected String maxLength(StringValidator validator) {
        if(validator.getMaxLength()==null){
            return "";
        }
        String message = validator.getMinLengthMessage();
        if(StringUtility.isNullOrEmpty(message)){
            message=String.format(StringValidator.defaultValidator.getMaxLengthMessage(), validator.getMaxLength());
        }
        return String.format(",\nv.maxLength(%1$s, %2$s)", validator.getMaxLength(), this.getMessage(validator, "max-length-message", message));
    }


    /**
     * v.check((val) => {return /^\d+$/.test(val);}, "请输入数字"),
     * * @return
     */
    protected String check(T validator, String regex, String message) {
        return String.format(",\nv.check((val) => {return %1$s.test(val);},%2$s)",
                regex,
                this.getMessage(validator, "check-message", message));
    }

    protected String transform(DigitalCategory category) {
        return String.format(",\nv.transform((input): number | string => {return %s;})", category.getConverter());
    }

    protected String minValue(DigitalValidator validator) {
        if (validator.getMinValue()==null) {
            return "";
        }
        String message = validator.getMinValueMessage();
        if(StringUtility.isNullOrEmpty(message)){
            message=String.format(DigitalValidator.defaultValidator().getMinValueMessage(), validator.getMinValue());
        }
        return String.format(",\nv.minValue(%1$s, %2$s)", validator.getMinValue(), this.getMessage(validator, "min-value-message", message));
    }

    protected String maxValue(DigitalValidator validator) {
        if (validator.getMaxValue()==null) {
            return "";
        }
        String message = validator.getMaxValueMessage();
        if(StringUtility.isNullOrEmpty(message)){
            message=String.format(DigitalValidator.defaultValidator().getMaxValueMessage(), validator.getMaxValue());
        }
        return String.format(",\n v.maxValue(%1$s, %2$s)", validator.getMaxValue(), this.getMessage(validator, "max-value-message", message));
    }

    protected abstract String outerGenerateMessage(String propertyName, T validator);

    public String generateConfig(String propertyName, T validator) {
        String message = this.outerGenerateMessage(propertyName, validator);
        return String.format("%1$s:\n%2$s\n", propertyName, message);
    }
}
