package org.example.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.Factory;
import org.example.RpcEnum;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof  RpcMessage){
            String messageType = ((RpcMessage) msg).getMessageType();
            if(messageType == RpcEnum.MESSAGE_REQUEST_TYPE){
                RpcRequest rpcRequest = (RpcRequest)((RpcMessage) msg).getData();
                RpcResponse rpcResponse = Factory.servicePrivider.serviceCaller(rpcRequest);
                RpcMessage rpcMessage = RpcMessage.builder().data(rpcResponse).messageType(RpcEnum.MESSAGE_RESPONSE_TYPE).build();
                ctx.writeAndFlush(rpcMessage);
            }
        }
    }
}
