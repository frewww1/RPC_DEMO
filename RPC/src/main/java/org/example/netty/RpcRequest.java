package org.example.netty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
@Builder
public class RpcRequest {
    private static final long serialVersionUID = 1905122041950251207L;
    public String methodName;
    public String serviceName;
    public Object[] args;
    public Class<?>[] argsType;
    private String requestId;


}
