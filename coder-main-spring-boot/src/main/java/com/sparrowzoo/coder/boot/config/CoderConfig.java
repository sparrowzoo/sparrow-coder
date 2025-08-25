package com.sparrowzoo.coder.boot.config;


import com.sparrow.authenticator.session.dao.EmptySessionDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CoderConfig {
    public CoderConfig() {
        log.info("CoderConfig init");
    }

    @Bean
    public EmptySessionDao sessionDao() {
        return new EmptySessionDao();
    }
}
