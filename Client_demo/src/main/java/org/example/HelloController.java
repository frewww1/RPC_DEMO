package org.example;

import org.example.annotation.RpcReference;
import org.springframework.context.annotation.Bean;


public class HelloController {
    @RpcReference
    public Hello hello;
    public void testHello(){

        System.out.println(hello.function(5));
    }
}
