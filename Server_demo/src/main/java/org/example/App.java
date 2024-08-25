package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.annotation.EnableRpc;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import java.util.Timer;
import java.util.TimerTask;

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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 模拟业务执行
//                System.out.println("正在执行业务...");

            }
        }, 0, 5 * 1000);
    }
}
