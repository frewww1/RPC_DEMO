package org.example.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.example.NettyServerChannelInitializer;

import java.net.InetSocketAddress;
import java.net.SocketPermission;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MyServer {
    private int port = 9999;
    private ServerBootstrap serverBootstrap = initBootstrap();
//    单例模式
    private static final MyServer SingleBean = new MyServer();

    private MyServer(){}

    private ServerBootstrap initBootstrap(){
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(NettyServerChannelInitializer.INSTANCE);
        return serverBootstrap;
    }
//    监听并接收消息
    public ChannelFuture start(){
        ChannelFuture channelFuture = serverBootstrap.bind();
        channelFuture.addListener((future) -> {
            try{
                System.out.println(channelFuture.channel().remoteAddress().toString());
                if (channelFuture.isSuccess()) {
                    System.out.println("监听端口成功");
                } else {
                    System.out.println("监听端口失败");
                }
            }catch (Exception e){
                // 处理异常，例如记录日志
                e.printStackTrace();
            }
            });
//        channelFuture.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                if(channelFuture.isSuccess()){
//                    log.info("成功监听");
//                }
//            }
//        });
        return channelFuture;
    }

    public static MyServer getSingleBean(){
        return SingleBean;
    }
    public static void main(String[] args) {
//        MyServer myServer = new MyServer();
//        使用单例模式
        MyServer myServer = MyServer.getSingleBean();
        myServer.start();
        System.out.println("监听开始!");
    }
}
