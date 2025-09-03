package com.sparrowzoo.coder.boot.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CoderConfig.class)
public @interface EnableCoderApp {
}
