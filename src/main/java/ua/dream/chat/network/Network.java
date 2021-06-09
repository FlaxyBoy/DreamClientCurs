package ua.dream.chat.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ua.dream.chat.App;
import ua.dream.chat.network.handlers.ClientHandler;
import ua.dream.chat.network.handlers.PacketProcessor;
import ua.dream.chat.network.netty.coodec.PacketDecoder;
import ua.dream.chat.network.netty.coodec.PacketEncoder;


public class Network {

    private SocketChannel channel;
    private final Thread thread;

    public Network(PacketProcessor processor) {
        thread = new Thread(() -> {
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                channel.pipeline().addLast(new PacketDecoder() , new PacketEncoder() , new ClientHandler(processor));
                            }
                        });
                while (App.enable.get()) {
                    Thread.sleep(1000);
                    ChannelFuture future = b.connect("mc.dreamnw.ru" , 8888).sync();
                    System.out.println("Conected");
                    future.channel().closeFuture().sync();
                }
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        });
        thread.start();
    }

    public void stop() {
        thread.stop();
    }

    public SocketChannel getChannel() {
        return channel;
    }
}
