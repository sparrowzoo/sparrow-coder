/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.coder.spel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public class CodeGenerator {
    public static void main(String[] args) {
        CodeGenerator generator = new CodeGenerator();
        System.out.println(generator.generateEntity(String.class));
    }

    private final TemplateEngine templateEngine;

    public CodeGenerator() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        StringTemplateResolver templateResolver= new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        springTemplateEngine.setTemplateResolver(templateResolver);
        springTemplateEngine.setDialect(new SpringStandardDialect());
        springTemplateEngine.setEnableSpringELCompiler(true);
        this.templateEngine = springTemplateEngine;
    }

    public String generateEntity(Class<?> clazz) {
        Context ctx = new Context();
        ctx.setVariable("className", clazz.getSimpleName());
        List<FieldInfo> fields = Arrays.stream(clazz.getDeclaredFields())
                .map(f -> new FieldInfo(f.getName(), f.getType().getSimpleName()))
                .collect(Collectors.toList());
        ctx.setVariable("fields", fields);
        ctx.setVariable("test", "<b>加粗</b>");
        return templateEngine.process("===== 内联表达式 =====\n" +
                        "[[${className}]]      -- HTML 转义\n" +
                        "[(${className})]      -- 不转义\n" +
                        "[[${test}]]      -- HTML 转义示例\n" +
                        "[(${test})]      -- 不转义示例\n" +
                        "\n" +
                        "===== 条件表达式 =====\n" +
                        "[# th:if=\"${className == 'String'}\"] className 是 String[/]\n" +
                        "[# th:if=\"${className == 'Integer'}\"] className 是 Integer[/]\n" +
                        "[# th:unless=\"${className == 'String'}\"] className 不是 String[/]\n" +
                        "三元: [(${className == 'String'} ? '是String' : '不是String')]\n" +
                        "[# th:switch=\"${className}\"]\n" +
                        "[# th:case=\"'String'\"]case: 是 String[/]\n" +
                        "[# th:case=\"'Integer'\"]case: 是 Integer[/]\n" +
                        "[# th:case=\"*\"]case: 默认[/]\n" +
                        "[/]\n" +
                        "\n" +
                        "===== 循环表达式 =====\n" +
                        "[# th:each=\"field : ${fields}\"]字段名=[[${field.name}]], 类型=[[${field.type}]]\n" +
                        "[/]",
                ctx);
    }

    public static class FieldInfo {
        private final String name;
        private final String type;

        public FieldInfo(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }
}
