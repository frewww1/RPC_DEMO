package org.example;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

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
        try {
            System.out.println("接收到的信息：" + msg);
            if ("ping".equals(msg.toString())){
                System.out.println("发送pong");
                ctx.channel().writeAndFlush("pong");
            }
            else{
                ctx.channel().writeAndFlush("我收到了");
            }
        } finally {
//            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        //空闲状态的事件
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            System.out.println(((IdleStateEvent) event).state() + ">>>" + ctx.channel().id());
            if (event.state().equals(IdleState.READER_IDLE)){
                // 心跳包丢失，10秒没有收到客户端心跳 (断开连接)
                System.out.println("发送ping");
                ctx.channel().writeAndFlush("ping");
//                System.out.println("已与 "+ctx.channel().remoteAddress()+" 断开连接");
            }
        }
    }
}
