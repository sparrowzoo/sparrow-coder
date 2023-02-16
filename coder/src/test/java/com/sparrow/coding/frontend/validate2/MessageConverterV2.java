package com.sparrow.coding.frontend.validate2;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.frontend.User;
import com.sparrow.coding.protocol.Form;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class MessageConverterV2 {
    /**
     * @param args
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //拿到需要解析的类
        Class userClazz = User.class;
        //通过反射拿到字段
        Field[] userFields = userClazz.getDeclaredFields();
        //定义要返回的json builder
        StringBuilder sb = new StringBuilder();
        //遍历每一个字段
        for (Field field : userFields) {
            //拿到form注解
            Form form = field.getAnnotation(Form.class);
            //拿到fieldName
            String fieldName = field.getName();
            //拿到定义的控件前缀
            String controlPrefix = form.type().getPrefix();
            //拿到定义的验证注解类
            Class annotationClass = form.validate();
            //通过验证注解类 拿到该对象的信息生成器
            ValidatorMessageGenerator validatorMessageGenerator = MessageGeneratorStrategyV2.getValidatorGenerator(annotationClass);
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
