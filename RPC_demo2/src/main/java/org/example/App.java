package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.annotation.EnableRpc;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * Hello world!
 *
 */
@EnableRpc
@Slf4j
@Import(HelloImpl.class)
public class App {
    public static void main( String[] args ) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        log.info("容器启动完成");
        while (true);
    }
}
