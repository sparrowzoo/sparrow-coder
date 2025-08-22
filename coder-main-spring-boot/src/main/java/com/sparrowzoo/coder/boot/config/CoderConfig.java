package com.sparrowzoo.coder.boot.config;


import com.sparrow.authenticator.session.dao.EmptySessionDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoderConfig {
    @Bean
    public EmptySessionDao sessionDao() {
        return new EmptySessionDao();
    }
}
