package com.sparrow.coder.boot.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CoderAutoConfiguration.class)
public @interface EnableCoderApp {
}
