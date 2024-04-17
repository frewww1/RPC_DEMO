package org.example.netty.ChannelInitializer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.netty.Handler.NettyServerHandler;
import org.example.netty.Handler.RpcDecoder;
import org.example.netty.Handler.RpcEncoder;
import org.example.netty.Handler.RpcServerHandler;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class NettyServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    public static final NettyServerChannelInitializer INSTANCE = new NettyServerChannelInitializer();

//NioSocketChannel，异步的客户端TCP Socket连接

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                //空闲状态的处理器
                .addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
//                .addLast(new LengthFieldBasedFrameDecoder(4*1028*1024,0,4,0,0))
                .addLast(new RpcEncoder())
                .addLast(new RpcDecoder())
                .addLast(NettyServerHandler.INSTANCE)
                .addLast(new RpcServerHandler());
    }

//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("handlerAdded:有新连接加入了++++......" + ctx.channel().remoteAddress().toString());
//    }
}

