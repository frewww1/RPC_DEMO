package org.example;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.example.pojo.UserImpl;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;

public class ProxyFactory_demo {

    public static void main(String[] args) {
        UserImpl target = new UserImpl();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                System.out.println("前处理");
//                Object target = methodInvocation.getThis();
                Method method = methodInvocation.getMethod();
                String methodName = method.getName();
                String className = method.getDeclaringClass().getSimpleName();
                Object[] args = methodInvocation.getArguments();
                Class<?>[]  parameterTypes = method.getParameterTypes();
                Object result= methodInvocation.proceed();
                System.out.println("后处理");
                return result;
            }
        });
        UserImpl user = (UserImpl)proxyFactory.getProxy(UserImpl.class.getClassLoader());
        user.function(2);
    }
}
