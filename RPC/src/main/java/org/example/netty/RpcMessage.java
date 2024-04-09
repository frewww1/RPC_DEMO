package org.example.netty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
@AllArgsConstructor
@Setter
@Getter
@Builder
public class RpcMessage {
    public String messageType;
    public Object data;
}
