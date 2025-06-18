package com.sparrowzoo.coder.moke.beans;

import com.sparrow.protocol.BeanCopier;
import com.sparrow.utility.SparrowBeanCopier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockConfiguration {
    @Bean
    public BeanCopier beanCopier() {
        return new SparrowBeanCopier();
    }
}
