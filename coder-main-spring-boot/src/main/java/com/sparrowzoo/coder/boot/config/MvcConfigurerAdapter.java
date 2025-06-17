package com.sparrowzoo.coder.boot.config;

import com.sparrow.mq.DefaultQueueHandlerMappingContainer;
import com.sparrow.mq.EventHandlerMappingContainer;
import com.sparrow.spring.starter.config.SparrowConfig;
import com.sparrow.spring.starter.filter.AccessMonitorFilter;
import com.sparrow.spring.starter.monitor.Monitor;
import com.sparrow.support.Authenticator;
import com.sparrow.support.DefaultAuthenticatorService;
import com.sparrow.support.IpSupport;
import com.sparrow.support.web.MonolithicLoginUserFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.inject.Inject;
import javax.servlet.Filter;

@Configuration
public class MvcConfigurerAdapter implements WebMvcConfigurer {
    @Inject
    private IpSupport ipSupport;
    @Autowired
    private SparrowConfig sparrowConfig;
    @Bean
    public Monitor monitor() {
        return new Monitor(this.ipSupport);
    }

    @Bean
    public AccessMonitorFilter accessMonitorFilter() {
        return new AccessMonitorFilter(monitor(), -99);
    }



    @Bean
    Authenticator authenticator() {
        SparrowConfig.Authenticator authenticatorConfig = this.sparrowConfig.getAuthenticator();
        return new DefaultAuthenticatorService(authenticatorConfig.getEncryptKey(),
                authenticatorConfig.getValidateDeviceId(),
                authenticatorConfig.getValidateStatus());
    }

    @Bean
    MonolithicLoginUserFilter loginTokenFilter() {
        SparrowConfig.Authenticator authenticatorConfig = this.sparrowConfig.getAuthenticator();
        SparrowConfig.Mvc mvc = this.sparrowConfig.getMvc();
        return new MonolithicLoginUserFilter(
                authenticator(),
                authenticatorConfig.getMockLoginUser(),
                authenticatorConfig.getTokenKey(),
                mvc.getSupportTemplateEngine(),
                authenticatorConfig.getExcludePatterns(),
                mvc.getAjaxPattens()
        );
    }

    @Bean
    public FilterRegistrationBean<Filter> loginTokenFilterBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(loginTokenFilter());
        // 一个* 不允许多个***
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("loginTokenFilter");
        filterRegistrationBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return filterRegistrationBean;
    }

    @Bean
    public EventHandlerMappingContainer eventHandlerMappingContainer() {
        return new DefaultQueueHandlerMappingContainer();
    }
}