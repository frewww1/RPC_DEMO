package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.annotation.EnableRpc;
import org.example.annotation.RpcReference;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * Hello world!
 *
 */
@Import(HelloController.class)
@EnableRpc
@Slf4j
public class App {


    public static void main( String[] args )
    {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        log.info("容器启动完成");
        applicationContext.getBean(HelloController.class).testHello();
    }
}
