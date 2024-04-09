package org.example.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.Factory;
import org.example.loadbalance.LoadBalance;
import org.example.loadbalance.loadbalancer.ConsistentHashLoadBalance;
import org.example.zookeeper.ZKClient;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
public class Client {
    public Bootstrap bootstrap = creatClient();
    public HashMap<InetSocketAddress,Channel> channelCache = new HashMap<InetSocketAddress,Channel>() ;
    private final LoadBalance loadBalance = new ConsistentHashLoadBalance();

    @SneakyThrows
    public Bootstrap creatClient() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcEncoder());
                        p.addLast(new RpcDecoder());
                        p.addLast(new RpcClientHandler());
                    }
                });
        log.info("netty client 开启");
        return bootstrap;
    }

    @SneakyThrows
    public Channel getChannel(RpcRequest rpcRequest){
        //获取地址，会调用zk的取获取地址
        String serviceName = rpcRequest.getServiceName();

        List<String> addrs = Factory.zkClient.serviceDiscover(serviceName);
        String addr = loadBalance.selectServiceAddress(addrs,rpcRequest);
        String[] parts = addr.split("/");
        String ipAddress = parts[parts.length - 1];
        parts = ipAddress.split(":");
        InetSocketAddress inetSocketAddress = new InetSocketAddress(parts[0],Integer.parseInt(parts[1]));
//        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",Integer.parseInt(parts[1]));

        //尝试从缓存中获取
        Channel channel = channelCache.get(inetSocketAddress);
        //如果没有就创建
        if (channel == null) {
            ChannelFuture channelFuture = bootstrap.connect(inetSocketAddress).sync();
            channel = channelFuture.channel();
            channelCache.put(inetSocketAddress,channel);
        }
        return  channel;

    }

    public RpcResponse send(RpcMessage rpcMessage){
        RpcRequest rpcRequest = (RpcRequest) rpcMessage.data;
        //获取channel
        Channel channel = getChannel(rpcRequest);
        //发送
        channel.writeAndFlush(rpcMessage);

        return Factory.unprocessedRequests.get(rpcRequest);

    }
}
