package com.sparrow.coder.boot.config;

import com.sparrow.spring.mvc.ControllerReturnAdvice;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;

@RestControllerAdvice(basePackages = {"com.sparrow.coder.adapter.controller"})
public class CoderControllerAdvice extends ControllerReturnAdvice {
}
