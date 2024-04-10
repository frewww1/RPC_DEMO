package org.example.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.NettyClientChannelInitializer;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor
public class MyClient {



    private static Bootstrap bootstrap = getBootstrap();
    private static Bootstrap getBootstrap(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new NettyClientChannelInitializer());
        return bootstrap;
    }

    public static ChannelFuture start(InetSocketAddress inetSocketAddress){
        ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress);
//        channelFuture.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                log.info("连接到服务器:"+channelFuture.channel().remoteAddress().toString());
//            }
//        });
        return channelFuture;
    }

    @SneakyThrows
    public void send(){
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",9999);
        ChannelFuture channelFuture = start(inetSocketAddress).sync();
        channelFuture.channel().writeAndFlush("我是客户端").sync();

    }

    public static void main(String[] args) {
        MyClient myClient = new MyClient();
        myClient.send();
    }
}
