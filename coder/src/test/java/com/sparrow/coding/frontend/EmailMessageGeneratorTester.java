package com.sparrow.coding.frontend;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.EmailValidatorMessageGenerator;
import com.sparrow.coding.protocol.Form;
import com.sparrow.coding.protocol.validate.EmailValidator;
import com.sparrow.coding.protocol.validate.EqualValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public class EmailMessageGeneratorTester {
    private static Map<String, Object> getAnnotationKeyValues(Annotation a) {
        return null;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Class userClazz = User.class;
        Field field = userClazz.getDeclaredField("confirmPassword");
        Form form = field.getAnnotation(Form.class);
        EqualValidator nullValidator = field.getAnnotation(EqualValidator.class);
        //Map<String, Object> keyValues = getAnnotationKeyValues(a);
        ValidatorMessageGenerator validatorMessageGenerator = new EmailValidatorMessageGenerator();
        String json = validatorMessageGenerator.generateValidateMessage(field.getName(), form.type().getPrefix(), nullValidator);
        System.out.println(json);
    }
}
