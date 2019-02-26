package com.sparrow.coding;

import com.sparrow.utility.StringUtility;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by harry on 2016/12/29.
 */
public class GenerateAssembleCode {
    public String generate(Class desc, Class src) {
        String srcObjectName = StringUtility.setFirstByteLowerCase(src.getSimpleName());
        String descObjectName = StringUtility.setFirstByteLowerCase(desc.getSimpleName());
        Method[] descMethods = desc.getDeclaredMethods();
        Set<String> existSet = new TreeSet<String>();
        Set<String> notExistSet = new TreeSet<String>();

        StringBuilder codeBuilder = new StringBuilder();

        codeBuilder.append(String.format("%1$s %2$s =new %1$s();\n", desc.getSimpleName(), descObjectName));


        for (Method method : descMethods) {
            if (!method.getName().startsWith("set")) {
                continue;
            }
            //descObjectName.setMethod(srcObjectName.getMethod());
            String descGetMethodName = method.getName().replace("set", "get");
            try {
                //src 类是否存在该方法名
                src.getMethod(descGetMethodName);
                existSet.add(String.format("%1$s.%2$s(%3$s.%4$s());\n", descObjectName, method.getName(), srcObjectName, descGetMethodName));
            } catch (NoSuchMethodException e) {
                Class returnType = null;
                try {
                    //desc 方法的返回类型
                    returnType = desc.getMethod(descGetMethodName).getReturnType();
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                    continue;
                }

                if (returnType.equals(Long.class)) {
                    notExistSet.add(String.format("%1$s.%2$s(0L);\n", descObjectName, method.getName()));
                    continue;
                }
                if (returnType.equals(Number.class)) {
                    notExistSet.add(String.format("%1$s.%2$s(0);\n", descObjectName, method.getName()));
                    continue;
                }
                if (returnType.equals(String.class)) {
                    notExistSet.add(String.format("%1$s.%2$s(SYMBOL.EMPTY);\n", descObjectName, method.getName()));
                    continue;
                }
                notExistSet.add(String.format("%1$s.%2$s(null);\n", descObjectName, method.getName()));
            }
        }

        for (String k : existSet) {
            codeBuilder.append(k);
        }
        for (String k : notExistSet) {
            codeBuilder.append(k);
        }
        codeBuilder.append("return " + descObjectName + ";");
        return codeBuilder.toString();
    }
}
