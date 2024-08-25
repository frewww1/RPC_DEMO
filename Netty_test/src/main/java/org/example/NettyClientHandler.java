package org.example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.example.test.MyClient;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

//处理输入信息
public class  NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        if (msg.toString()=="ping"){
            System.out.println("发送pong");
            ctx.channel().writeAndFlush("pong");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接激活 == " + ctx.channel().remoteAddress().toString());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if(!ctx.channel().isOpen()){
            System.out.println("通道关闭了");
        }else{
            System.out.println("断线了......" + ctx.channel().remoteAddress().toString());
            ctx.channel().eventLoop().schedule(() -> {
                System.out.println("断线重连......");
                //重连
                MyClient.start((InetSocketAddress)ctx.channel().remoteAddress());
            }, 3L, TimeUnit.SECONDS);
        }
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
            if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
                System.out.println("空闲"  + ctx.channel().id());
                //发送ping 保持心跳链接
                System.out.println("发送ping");

                ctx.writeAndFlush("ping");
            }
        }else {
            userEventTriggered(ctx,evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}


