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

import com.sparrow.coder.domain.bo.ProjectBO;
import com.sparrow.coder.domain.bo.ProjectConfigBO;
import com.sparrow.coder.domain.bo.TableConfigBO;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.utility.FileUtility;
import com.sparrow.utility.StringUtility;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Map;

public class PageGenerator {
    public static void main(String[] args) {
        PageGenerator generator = new PageGenerator();
        System.out.println(generator.generateEntity(String.class));
    }

    private final TemplateEngine templateEngine;

    public PageGenerator() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        springTemplateEngine.setTemplateResolver(templateResolver);
        springTemplateEngine.setDialect(new SpringStandardDialect());
        springTemplateEngine.setEnableSpringELCompiler(true);
        this.templateEngine = springTemplateEngine;
    }

    public String generateEntity(Class<?> clazz) {
        TableContext context=new TableContext();
        ProjectBO project=new ProjectBO();
        context.setProject(project);
        context.setTableConfig(new TableConfigBO());
        context.getTableConfig().setIsTree(true);
        Context ctx = new Context();
        ctx.setVariable("ctx", context);
        ctx.setVariable("var",false);


        String pageContent = FileUtility.getInstance().readFileContent("/page-test.txt");
        String pageContent2="[# th:if=\"${var}\"] TRUE[/][# th:unless=\"${var}\"] FALSE[/]";
        return templateEngine.process(pageContent2,
                ctx);
    }
}
