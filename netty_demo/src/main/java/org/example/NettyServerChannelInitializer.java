package org.example;

@ChannelHandler.Sharable
public class NettyServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    public static final NettyServerChannelInitializer INSTANCE = new NettyServerChannelInitializer();

//NioSocketChannel，异步的客户端TCP Socket连接

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                //空闲状态的处理器
                .addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS))
                .addLast(new ObjectEncoder())
                .addLast(new ObjectDecoder((s) -> {
                    return String.class;
                }))
                .addLast(NettyServerHandler.INSTANCE);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有新连接加入了++++......" + ctx.channel().id());
    }
}

