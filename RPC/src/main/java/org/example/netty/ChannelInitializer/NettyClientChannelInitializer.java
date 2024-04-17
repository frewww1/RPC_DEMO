package org.example.netty.ChannelInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.example.netty.Handler.NettyClientHandler;
import org.example.netty.Handler.RpcClientHandler;
import org.example.netty.Handler.RpcDecoder;
import org.example.netty.Handler.RpcEncoder;

import java.util.concurrent.TimeUnit;

public class NettyClientChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                //空闲状态的处理器
                .addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                .addLast(new RpcEncoder())
                .addLast(new RpcDecoder())
                .addLast(new NettyClientHandler())
                .addLast(new RpcClientHandler());
    }
}
