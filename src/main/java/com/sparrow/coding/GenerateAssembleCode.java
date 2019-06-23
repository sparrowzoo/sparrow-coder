package com.sparrow.coding;

import com.sparrow.utility.StringUtility;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by harry on 2016/12/29.
 */
public class GenerateAssembleCode {

    public StringBuilder generate(Class dest, Class src) {
        String srcObjectName = StringUtility.setFirstByteLowerCase(src.getSimpleName());
        String descObjectName = StringUtility.setFirstByteLowerCase(dest.getSimpleName());
        Method[] descMethods = dest.getDeclaredMethods();
        //在目录类中存在的方法
        Set<String> existSet = new TreeSet<String>();
        //在目录类中不存在该方法
        Set<String> notExistSet = new TreeSet<String>();

        StringBuilder codeBuilder = new StringBuilder();

        codeBuilder.append(String.format("%1$s %2$s =new %1$s();\n", dest.getSimpleName(), descObjectName));

        for (Method method : descMethods) {
            if (!method.getName().startsWith("set")) {
                continue;
            }
            //descObjectName.setMethod(srcObjectName.getMethod());
            String destGetMethodName = method.getName().replace("set", "get");
            try {
                //src 类是否存在该方法名
                src.getMethod(destGetMethodName);
                existSet.add(String.format("%1$s.%2$s(%3$s.%4$s());\n", descObjectName, method.getName(), srcObjectName, destGetMethodName));
            } catch (NoSuchMethodException e) {
                Class returnType = null;
                try {
                    //dest 方法的返回类型
                    returnType = dest.getMethod(destGetMethodName).getReturnType();
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                    continue;
                }

                if (returnType.equals(Long.class)) {
                    notExistSet.add(String.format("%1$s.%2$s(0L);\n", descObjectName, method.getName()));
                    continue;
                }
                if (Number.class.isAssignableFrom(returnType)) {
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
        return codeBuilder.append("return " + descObjectName + ";");
    }
}
