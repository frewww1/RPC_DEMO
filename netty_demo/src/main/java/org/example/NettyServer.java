package org.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    public static final int port = 8787;

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.run();
    }


    private void run() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap().group(boss, work);
            bootstrap.channel(NioServerSocketChannel.class)
                    //业务处理
                    .childHandler(NettyServerChannelInitializer.INSTANCE);

            ChannelFuture future = bootstrap.bind(port).sync();

            future.addListener(f -> {
                if (future.isSuccess()) {
                    System.out.println("服务启动成功");
                } else {
                    System.out.println("服务启动失败");
                }
            });
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}

