package com.sparrow.coding.frontend;

import com.sparrow.coding.api.ValidatorMessageGenerator;
import com.sparrow.coding.config.ExampleFront;
import com.sparrow.coding.frontend.validate.valibot.StringValidatorMessageGenerator;
import com.sparrow.coding.protocol.ColumnDef;
import com.sparrow.coding.protocol.validate.AllowInputCharLengthValidator;
import com.sparrow.coding.protocol.validate.AllowOptionsValidator;
import com.sparrow.coding.protocol.validate.ChineseCharactersValidator;
import com.sparrow.coding.protocol.validate.DigitalValidator;
import com.sparrow.coding.protocol.validate.EmailValidator;
import com.sparrow.coding.protocol.validate.EqualValidator;
import com.sparrow.coding.protocol.validate.IdCardValidator;
import com.sparrow.coding.protocol.validate.MobileValidator;
import com.sparrow.coding.protocol.validate.StringValidator;
import com.sparrow.coding.protocol.validate.TelValidator;
import com.sparrow.coding.protocol.validate.UserNameRuleValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class MessageGeneratorTester {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        test("userName", UserNameRuleValidator.class);
        test("age", DigitalValidator.class);
        test("password", StringValidator.class);
        test("status", AllowOptionsValidator.class);
        test("name", ChineseCharactersValidator.class);
        test("email", EmailValidator.class);
        test("idCard", IdCardValidator.class);
        test("mobile", MobileValidator.class);
        test("tel", TelValidator.class);
        test("confirmPassword", EqualValidator.class);
        test("remark", AllowInputCharLengthValidator.class);
    }

    private static void test(String fieldName,
        Class annotation) throws NoSuchFieldException, IllegalAccessException {
        Class userClazz = ExampleFront.class;
        Field field = userClazz.getDeclaredField(fieldName);
        ColumnDef form = field.getAnnotation(ColumnDef.class);
        Annotation validator = field.getAnnotation(annotation);
        ValidatorMessageGenerator validatorMessageGenerator = new StringValidatorMessageGenerator();
        String json = validatorMessageGenerator.generateValidateMessage(field.getName(), form.type().getPrefix(), validator);
        System.out.println(json);
    }
}
