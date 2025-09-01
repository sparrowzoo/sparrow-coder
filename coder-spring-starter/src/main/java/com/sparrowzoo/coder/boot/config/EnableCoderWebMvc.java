package com.sparrowzoo.coder.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@ComponentScan(
        basePackages = {"com.sparrow.coder"}
)
@ConditionalOnWebApplication
public @interface EnableCoderWebMvc {
}
