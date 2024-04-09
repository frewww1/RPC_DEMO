package org.example.netty;

import lombok.SneakyThrows;
import org.example.RpcEnum;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ServicePrivider {
    private final HashMap<String,Object> services = new HashMap<>();
    public void register(String serviceName ,Object bean){
        services.put(serviceName,bean);
    }

    public Object getServiceClass(String serviceName){
        return services.get(serviceName);
    }
    @SneakyThrows
    public RpcResponse serviceCaller(RpcRequest rpcRequest){
        String serviceName = rpcRequest.getServiceName();
        Object[] args = rpcRequest.getArgs();
        String requestId = rpcRequest.getRequestId();
        Class<?>[] argsType = rpcRequest.getArgsType();
        String methodName = rpcRequest.getMethodName();
        Object bean = getServiceClass(serviceName);
        Method method = bean.getClass().getMethod(methodName,argsType);

        Object data = method.invoke(bean,args);
        RpcResponse rpcResponse;
        if(data ==null){
            rpcResponse = RpcResponse.builder()
                    .requestId(requestId).data(data).code(RpcEnum.RESPNSE_CODE_FAIL)
                    .build();
        }
        else{
            rpcResponse = RpcResponse.builder()
                .requestId(requestId).data(data).code(RpcEnum.RESPNSE_CODE_SUCESS)
                .build();
        }

        return rpcResponse;
    }
}
