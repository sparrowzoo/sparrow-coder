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

package com.sparrow.coder.domain.service.template;

import com.sparrow.utility.StringUtility;
import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.TemplateEngineer;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Map;

public class DefaultTemplateEngineer implements TemplateEngineer {
    private final TemplateEngine templateEngine;

    public DefaultTemplateEngineer() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        springTemplateEngine.setTemplateResolver(templateResolver);
        springTemplateEngine.setDialect(new SpringStandardDialect());
        springTemplateEngine.setEnableSpringELCompiler(true);
        this.templateEngine = springTemplateEngine;
    }

    @Override
    public String generate(String template, TableContext context, TableConfigRegistry registry) {
        String content = StringUtility.replace(template, context.getPlaceHolder());
        Context ctx = new Context();
        ctx.setVariable("ctx", content);
        ctx.setVariable("registry", registry);
        Map<String, String> map = context.getPlaceHolder();
        for (String key : map.keySet()) {
            ctx.setVariable(key.substring(1), map.get(key));
        }
        Map<String, Object> data = context.getVariables();
        for (String key : data.keySet()) {
            ctx.setVariable(key, data.get(key));
        }
        return templateEngine.process(content, ctx);
    }
}
