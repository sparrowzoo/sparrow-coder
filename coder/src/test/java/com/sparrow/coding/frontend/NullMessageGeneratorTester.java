package com.sparrow.coding.frontend;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.NullValidatorMessageGenerator;
import com.sparrow.coding.protocol.Form;
import com.sparrow.coding.protocol.validate.EqualValidator;
import com.sparrow.coding.protocol.validate.NullValidator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

public class NullMessageGeneratorTester {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Class userClazz = User.class;
        Field field = userClazz.getDeclaredField("confirmPassword");
        Form form = field.getAnnotation(Form.class);
        EqualValidator nullValidator = field.getAnnotation(EqualValidator.class);

        InvocationHandler invocationHandler = Proxy.getInvocationHandler(nullValidator);

//        Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
//        memberValues.setAccessible(true);
//        Map<String, Object> maps = (Map<String, Object>) memberValues.get(invocationHandler);
//
//        for (String key : maps.keySet()) {
//            System.out.println(key);
//            System.out.println(maps.get(key));
//        }

//        Field []fields= nullValidator.getClass().getFields();
//AnnotationInvo handlerAnnotationInfo=new HandlerAnnotationInfo();
//        ((Proxy) nullValidator).
//
        ValidatorMessageGenerator validatorMessageGenerator = new NullValidatorMessageGenerator();
        String json = validatorMessageGenerator.generateValidateMessage(field.getName(), form.type().getPrefix(), nullValidator);
        System.out.println(json);
    }
}
