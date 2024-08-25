package org.example;

import org.example.pojo.User;
import org.example.pojo.UserImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDK_proxy {
    public static void main(String[] args) {
//        先要取到被代理对象
        UserImpl target = new UserImpl();
        Object proxy = Proxy.newProxyInstance(UserImpl.class.getClassLoader(), new Class[]{User.class}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("前处理");
                Object result= method.invoke(target,args);
                System.out.println("后处理");
                return result;
            }
        });
        User userProxy = (User) proxy;
        userProxy.function(2);
    }

}
