package org.example.netty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Builder
@Setter
@Getter
public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;
    private String requestId;


    private String code;

    private Object data;
}
