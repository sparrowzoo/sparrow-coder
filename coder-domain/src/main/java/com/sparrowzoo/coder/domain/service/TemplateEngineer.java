package com.sparrowzoo.coder.domain.service;

import com.sparrowzoo.coder.domain.bo.TableContext;
import com.sparrowzoo.coder.domain.service.registry.TableConfigRegistry;

public interface TemplateEngineer {
    String generate(String template, TableContext context, TableConfigRegistry registry);
}
