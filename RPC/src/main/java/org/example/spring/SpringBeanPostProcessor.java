package org.example.spring;

import lombok.SneakyThrows;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.example.Factory;
import org.example.RpcEnum;
import org.example.annotation.RpcReference;
import org.example.annotation.RpcService;
import org.example.netty.Client;
import org.example.netty.Pojo.RpcMessage;
import org.example.netty.Pojo.RpcRequest;
import org.example.netty.Pojo.RpcResponse;
import org.example.netty.Server;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.UUID;

public class SpringBeanPostProcessor implements BeanPostProcessor {
    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //注册服务，在本地记录服务类以及zk注册
        if(bean.getClass().isAnnotationPresent(RpcService.class)){
            if(Factory.server == null){Factory.server = new Server();}
            String serviceName = bean.getClass().getInterfaces()[0].getSimpleName();
            String host = InetAddress.getLocalHost().getHostAddress();
            Factory.servicePrivider.register(serviceName,bean);
            String path = "/RPC/"+serviceName+"/"+host+":"+Factory.server.port;
            Factory.zkClient.serviceRegister(path);


        }
        //对RpcReference注解的属性进行代理，代理会去通过服务名发现服务获取host，并使用netty发送请求。并将获取的响应返回。
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for(Field field : declaredFields){
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if(rpcReference != null) {
                if(Factory.client == null){Factory.client = new Client();}

                //生成field的代理对象
                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setTargetClass(field.getType());
                proxyFactory.addAdvice(new MethodInterceptor() {
                    @Override
                    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                        //获取方法对象，得到方法名字、类名、参数对象以及参数类型
                        Method method = methodInvocation.getMethod();
                        String methodName = method.getName();
                        String interfaceName = method.getDeclaringClass().getSimpleName();
                        Object[] args = methodInvocation.getArguments();
                        Class<?>[]  argsType = method.getParameterTypes();
                        //构造请求类
                        RpcRequest rpcRequest = RpcRequest.builder().methodName(methodName)
                                .serviceName(interfaceName).args(args).argsType(argsType).requestId(UUID.randomUUID().toString())
                                .build();
                        RpcMessage rpcMessage = RpcMessage.builder().messageType(RpcEnum.MESSAGE_REQUEST_TYPE).data(rpcRequest).build();
                        //发送数据,接受数据，并返回
                        Class<?> returnType = method.getReturnType();
                        if(returnType == int.class){
                            returnType = Integer.class;
                        }
                        RpcResponse rpcResponse = Factory.client.send(rpcMessage);
                        return returnType.cast(rpcResponse.getData()) ;

                    }
                });
                Object proxy = proxyFactory.getProxy();
                //将该代理对象设置到field上
                try {
                    field.set(bean,proxy);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }


        }
        return bean;
    }
}
