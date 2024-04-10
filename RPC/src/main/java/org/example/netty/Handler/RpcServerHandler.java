package org.example.netty.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.example.Factory;
import org.example.RpcEnum;
import org.example.netty.Pojo.RpcMessage;
import org.example.netty.Pojo.RpcRequest;
import org.example.netty.Pojo.RpcResponse;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof RpcMessage){
            String messageType = ((RpcMessage) msg).getMessageType();
            if(messageType.equals(RpcEnum.MESSAGE_REQUEST_TYPE) ){

                try {
                    RpcRequest rpcRequest = (RpcRequest)((RpcMessage) msg).getData();
                    RpcResponse rpcResponse = Factory.servicePrivider.serviceCaller(rpcRequest);
                    RpcMessage rpcMessage = RpcMessage.builder().data(rpcResponse).messageType(RpcEnum.MESSAGE_RESPONSE_TYPE).build();
                    ctx.writeAndFlush(rpcMessage);
                }
                finally {
                    ReferenceCountUtil.release(msg);
                    return;
                }
            }
            ctx.fireChannelRead(msg);
        }
    }
}
