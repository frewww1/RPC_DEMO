package org.example;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.example.pojo.User;
import org.example.pojo.UserImpl;

import java.lang.reflect.Method;

public class Cglib_proxy {
    public static void main(String[] args) {
        UserImpl target = new UserImpl();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserImpl.class);
        enhancer.setCallbacks(new Callback[]{new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("前处理");
                Object result= methodProxy.invoke(target,objects);
                System.out.println("后处理");
                return result;
            }
        }});

        UserImpl user = (UserImpl)enhancer.create();
        user.function(2);
    }
}
