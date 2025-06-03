package com.sparrow.coding.frontend.validate1;

import com.sparrow.coding.config.ExampleFront;
import com.sparrow.coding.frontend.validate.valibot.StringValidatorMessageGenerator;
import com.sparrow.coding.protocol.ControlType;
import com.sparrow.coding.protocol.ColumnDef;
import com.sparrow.coding.protocol.validate.EmailValidator;
import com.sparrow.coding.protocol.validate.StringValidator;
import java.lang.reflect.Field;

public class MessageConverterV1 {
    @ColumnDef(text = "密码", type = ControlType.INPUT_PASSWORD, validate = StringValidator.class)
    @StringValidator(prompt = "请输入密码", nullError = "用户密码不允许为空", allowNull = false, minLength = 6, maxLength = 20, lengthError = "密码要求6-20位字符")
    private String password;

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

            /**
             *     @Form(text = "密码", type = ControlType.INPUT_PASSWORD, validate = NullValidator.class)
             *     @NullValidator(prompt = "请输入密码", nullError = "用户密码不允许为空", allowNull = false, minLength = 6, maxLength = 20, lengthError = "密码要求6-20位字符")
             *     private String password;
             */
            if (annotationClass.equals(StringValidator.class)) {
                StringValidator nullValidator = field.getAnnotation(StringValidator.class);
                String fieldJson = new StringValidatorMessageGenerator().generateValidateMessage(fieldName, controlPrefix, nullValidator);
                sb.append(fieldJson);
            } else if (annotationClass.equals(EmailValidator.class)) {
                EmailValidator emailValidator = field.getAnnotation(EmailValidator.class);
                String fieldJson = new StringValidatorMessageGenerator().generateValidateMessage(fieldName, controlPrefix, emailValidator);
                sb.append(fieldJson);
            }
            System.out.println(sb.toString());
            /**
             * ....
             */
        }
    }
}
