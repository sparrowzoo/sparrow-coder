package com.sparrow.coding.frontend;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.frontend.validate.DigitalValidatorMessageGenerator;
import com.sparrow.coding.protocol.Form;
import com.sparrow.coding.protocol.validate.DigitalValidator;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DigitalMessageGeneratorTester {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Class userClazz = User.class;
        Field field = userClazz.getDeclaredField("age");
        Form form = field.getAnnotation(Form.class);
        DigitalValidator digitalValidator = field.getAnnotation(DigitalValidator.class);

        ValidatorMessageGenerator validatorMessageGenerator = new DigitalValidatorMessageGenerator();
        String json = validatorMessageGenerator.generateValidateMessage(field.getName(), form.type().getPrefix(), digitalValidator);
        System.out.println(json);
    }

    private Map<String, Object> getKeyValue(DigitalValidator digitalValidator) {

        Map<String, Object> map = new HashMap<>();
        //digitalValidator
        return null;
    }
}
