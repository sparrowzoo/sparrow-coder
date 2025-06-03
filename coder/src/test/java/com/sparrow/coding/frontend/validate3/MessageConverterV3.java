package com.sparrow.coding.frontend.validate3;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.config.ExampleFront;
import com.sparrow.coding.frontend.validate.valibot.EmailValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.valibot.MobileValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.valibot.StringValidatorMessageGenerator;
import com.sparrow.coding.protocol.ColumnDef;
import com.sparrow.coding.protocol.validate.EmailValidator;
import com.sparrow.coding.protocol.validate.MobileValidator;
import com.sparrow.coding.protocol.validate.StringValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 策略和实验类基本稳定，支持扩展
 */
public class MessageConverterV3 {
    /**
     * @param args
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //拿到需要解析的类
        Class userClazz = ExampleFront.class;
        //通过反射拿到字段
        Field[] userFields = userClazz.getDeclaredFields();

        MessageGeneratorStrategyV3.put(StringValidator.class, new StringValidatorMessageGenerator());
        MessageGeneratorStrategyV3.put(EmailValidator.class, new EmailValidatorMessageGenerator());
        MessageGeneratorStrategyV3.put(MobileValidator.class, new MobileValidatorMessageGenerator());

        //定义要返回的json builder
        StringBuilder sb = new StringBuilder();
        //遍历每一个字段
        for (Field field : userFields) {
            //拿到form注解
            ColumnDef form = field.getAnnotation(ColumnDef.class);
            //拿到fieldName
            String fieldName = field.getName();
            //拿到定义的控件前缀
            String controlPrefix = form.type().getPrefix();
            //拿到定义的验证注解类
            Class annotationClass = form.validate();
            //通过验证注解类 拿到该对象的信息生成器
            ValidatorMessageGenerator validatorMessageGenerator = MessageGeneratorStrategyV3.getValidatorGenerator(annotationClass);
            //拿到验证类的实例
            Annotation validator = field.getAnnotation(annotationClass);
            //通过信息生成器生成验证提示信息
            String json = validatorMessageGenerator.generateValidateMessage(fieldName, controlPrefix, validator);
            //加入到提示信息的json中...
            sb.append(json);
        }
        System.out.println(sb.toString());
    }
}
