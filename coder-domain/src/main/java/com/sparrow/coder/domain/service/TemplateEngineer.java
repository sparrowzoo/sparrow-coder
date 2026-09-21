package com.sparrow.coder.domain.service;

import com.sparrow.coder.domain.bo.TableContext;
import com.sparrow.coder.domain.service.registry.TableConfigRegistry;

public interface TemplateEngineer {
    String generate(String template, TableContext context, TableConfigRegistry registry);
}
