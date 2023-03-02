package com.sparrow.coding;

import com.sparrow.example.po.SparrowExample;
import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.StringUtility;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PojoSourceTest {
    public static void main(String[] args) {
        List<Field> fields = ClassUtility.extractFields(SparrowExample.class);
        StringBuilder fieldBuild = new StringBuilder();
        StringBuilder methodBuild = new StringBuilder();
        for (Field field : fields) {
            Class<?> fieldClazz = field.getType();
            String upperField = StringUtility.setFirstByteUpperCase(field.getName());
            fieldBuild.append(String.format("private %s %s; \n", fieldClazz.getSimpleName(), field.getName()));
            methodBuild.append(String.format("public %2$s get%1$s(){\n return this.%3$s;\n}\n", upperField, fieldClazz.getSimpleName(), field.getName()));
            methodBuild.append(String.format("public void set%1$s(%2$s %3$s){\nthis.%3$s=%3$s;\n}\n", upperField, fieldClazz.getSimpleName(), field.getName()));
        }
        System.out.println(fieldBuild.append(methodBuild));
    }
}
