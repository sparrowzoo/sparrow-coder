package com.sparrowzoo.coder.domain.service;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerAware;
import com.sparrow.utility.ClassUtility;
import com.sparrowzoo.coder.domain.service.registry.ArchitectureRegistry;
import com.sparrowzoo.coder.domain.service.registry.PlaceholderExtensionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public abstract class AbstractPlaceholderExtension implements PlaceholderExtension, InitializingBean, ContainerAware {
    protected PlaceholderExtensionRegistry placeholderExtensionRegistry = PlaceholderExtensionRegistry.getInstance();

    @Override
    public void afterPropertiesSet() {
        String beanName = ClassUtility.getBeanNameByClass(this.getClass(), PlaceholderExtension.class);
        this.placeholderExtensionRegistry.pubObject(beanName, this);
    }

    @Override
    public void aware(Container container, String s) {
        this.afterPropertiesSet();
    }
}
