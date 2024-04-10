package org.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.netty.ChannelInitializer.NettyServerChannelInitializer;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
@NoArgsConstructor
public class Server {
    public int port = 9999;
    private ServerBootstrap serverBootstrap = initBootstrap();

    @SneakyThrows
    private ServerBootstrap initBootstrap(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
//                .localAddress(new InetSocketAddress( "127.0.0.1",port))
                .childHandler(NettyServerChannelInitializer.INSTANCE);
        ChannelFuture cf = serverBootstrap.bind(port);

        cf.addListener((future) -> {

            if (cf.isSuccess()) {
                System.out.println(cf.channel().localAddress().toString());
                System.out.println("监听端口成功");
            } else {
                System.out.println("监听端口失败");
            }});


        return serverBootstrap;
    }


}
