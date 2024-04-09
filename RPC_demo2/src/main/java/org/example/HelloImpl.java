package org.example;

import org.example.annotation.RpcService;

@RpcService
public class HelloImpl implements Hello{
    @Override

    public int function(int i) {
        return i+1;
    }
}
