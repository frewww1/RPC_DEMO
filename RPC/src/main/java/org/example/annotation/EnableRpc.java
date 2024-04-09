package org.example.annotation;


import org.example.spring.SpringBeanPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})

@Import({SpringBeanPostProcessor.class})
public @interface EnableRpc {
}
