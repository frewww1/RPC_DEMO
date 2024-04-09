package org.example.netty;

import lombok.SneakyThrows;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UnprocessedRequests {
    private final Map<String, CompletableFuture<RpcResponse>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    @SneakyThrows
    public RpcResponse get(RpcRequest rpcRequest){
        UNPROCESSED_RESPONSE_FUTURES.put(rpcRequest.getRequestId(), new CompletableFuture<RpcResponse>());
        return UNPROCESSED_RESPONSE_FUTURES.get(rpcRequest.getRequestId()).get();

    }
    @SneakyThrows
    public void complete(RpcResponse rpcResponse){
        CompletableFuture<RpcResponse> future = UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
        if(null != future){
            future.complete(rpcResponse);
        }

    }
}
