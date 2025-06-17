package com.sparrowzoo.coder.boot.config;

import com.sparrow.spring.starter.advice.ControllerResponseAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.sparrowzoo.coder.adapter.controller"})
public class ControllerAdvice extends ControllerResponseAdvice {
}