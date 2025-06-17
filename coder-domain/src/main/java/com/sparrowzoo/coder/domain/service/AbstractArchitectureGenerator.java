package com.sparrowzoo.coder.domain.service;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.utility.ClassUtility;
import com.sparrowzoo.coder.domain.service.registry.ArchitectureRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public abstract class AbstractArchitectureGenerator implements ArchitectureGenerator, InitializingBean, ContainerAware {
    protected ArchitectureRegistry architectureRegistry = ArchitectureRegistry.getInstance();

    @Override
    public void afterPropertiesSet() {
        String validatorName = ClassUtility.getBeanNameByClass(this.getClass());
        this.architectureRegistry.register(validatorName, this);
    }

    @Override
    public void aware(Container container, String s) {
        String validatorName = ClassUtility.getBeanNameByClass(this.getClass());
        this.architectureRegistry.register(validatorName, this);
    }
}
