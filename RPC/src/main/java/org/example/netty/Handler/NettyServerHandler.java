package org.example.netty.Handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.example.RpcEnum;
import org.example.netty.Pojo.RpcMessage;

@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    public static final NettyServerHandler INSTANCE = new NettyServerHandler();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive:有新连接加入了++++......" + ctx.channel().remoteAddress().toString());

        super.channelActive(ctx);
    }

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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        //空闲状态的事件
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
}
