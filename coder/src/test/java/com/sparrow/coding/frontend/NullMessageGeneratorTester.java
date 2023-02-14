package com.sparrow.coding.frontend;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.NullMessageGenerator;
import com.sparrow.coding.protocol.Form;
import com.sparrow.coding.protocol.validate.NullValidator;
import java.lang.reflect.Field;

public class NullMessageGeneratorTester {
    public static void main(String[] args) throws NoSuchFieldException {
        Class userClazz = User.class;
        Field field = userClazz.getDeclaredField("password");
        Form form = field.getAnnotation(Form.class);
        NullValidator nullValidator = field.getAnnotation(NullValidator.class);

        ValidatorMessageGenerator validatorMessageGenerator = new NullMessageGenerator();
        String json = validatorMessageGenerator.generateValidateMessage(field.getName(), form.type().getPrefix(), nullValidator);
        System.out.println(json);
    }
}
