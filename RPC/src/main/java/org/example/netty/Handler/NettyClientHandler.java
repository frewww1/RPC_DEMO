package org.example.netty.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.example.Factory;
import org.example.RpcEnum;
import org.example.netty.Pojo.RpcMessage;


import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


public class  NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //只负责处理ping
        if(msg instanceof RpcMessage){
            String messageType = ((RpcMessage) msg).getMessageType();
            if(messageType.equals(RpcEnum.MESSAGE_PING_TYPE) ){
                try {
                    RpcMessage rpcMessage = RpcMessage.builder().messageType(RpcEnum.MESSAGE_PONG_TYPE).build();
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接激活 == " + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断线了......" + ctx.channel().remoteAddress().toString());
        ctx.channel().eventLoop().schedule(() -> {
            System.out.println("断线重连......");
            //重连

            Factory.client.reConnect((InetSocketAddress)ctx.channel().remoteAddress());
        }, 3L, TimeUnit.SECONDS);
    }

    /**
     * 用户事件的回调方法
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果是空闲状态事件

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;


            if (event.state().equals(IdleState.READER_IDLE)){
                try {
                    RpcMessage rpcMessage = RpcMessage.builder().messageType(RpcEnum.MESSAGE_PONG_TYPE).build();
                    ctx.writeAndFlush(rpcMessage);
                }
                finally {
                    ReferenceCountUtil.release(evt);
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}


