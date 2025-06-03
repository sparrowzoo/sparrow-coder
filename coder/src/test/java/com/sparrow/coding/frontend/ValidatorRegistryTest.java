package com.sparrow.coding.frontend;

import com.sparrow.coding.api.ValidatorRegistry;
import com.sparrow.coding.config.ExampleFront;
import com.sparrow.coding.protocol.ColumnDef;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class ValidatorRegistryTest {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder().initController(false)
            .initInterceptor(false)
            .scanBasePackage("com.sparrow"));

        Class userClazz = ExampleFront.class;
        Field[] fields = userClazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            try {
                ColumnDef form = field.getAnnotation(ColumnDef.class);
                Annotation validator = field.getAnnotation(form.validate());
                String fieldJson = ValidatorRegistry.getInstance().getValidatorMessageGenerator(validator.annotationType())
                    .generateValidateMessage(field.getName(), form.type().getPrefix(), validator);
                sb.append(fieldJson);
            } catch (Exception e) {
                System.out.println(field.getName() + "-" + e.toString());
            }
        }
        System.out.println(sb.toString());
    }
}
