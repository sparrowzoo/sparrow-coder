package com.sparrowzoo.coder.domain.service.template;

import com.sparrow.utility.StringUtility;
import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.TemplateEngineer;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.dialect.SpringStandardDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Map;

public class DefaultTemplateEngineer implements TemplateEngineer {
    private final TemplateEngine templateEngine;

    public DefaultTemplateEngineer() {
        SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        StringTemplateResolver templateResolver=new StringTemplateResolver();
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
