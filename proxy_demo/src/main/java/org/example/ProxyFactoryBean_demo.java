package org.example;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.example.pojo.User;
import org.example.pojo.UserImpl;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ProxyFactoryBean_demo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ProxyFactoryBean_demo.class);
        Object o = applicationContext.getBean(User.class);
        User user = (User)o;
        user.function(2);
    }
    @Bean
    public ProxyFactoryBean userProxy(){
        UserImpl target = new UserImpl();
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(target);
        proxyFactoryBean.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                System.out.println("前处理");
                Object result= methodInvocation.proceed();
                System.out.println("后处理");
                return result;
            }
        });
        return proxyFactoryBean;
    }
}
