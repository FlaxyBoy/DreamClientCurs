package ua.dream.chat.network;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.jetbrains.annotations.NotNull;
import ua.dream.chat.network.handlers.ClientHandler;
import ua.dream.chat.network.handlers.PacketProcessor;
import ua.dream.chat.network.netty.coodec.PacketDecoder;
import ua.dream.chat.network.netty.coodec.PacketEncoder;
import ua.dream.chat.utils.validate.Check;

public class DreamClient {

    public static final String DECODER_HANDLER_NAME = "decoder"; // Read
    public static final String ENCODER_HANDLER_NAME = "encoder"; // Write
    public static final String CLIENT_CHANNEL_NAME = "handler"; // Read

    private boolean initialized = false;

    private EventLoopGroup worker;
    private Bootstrap bootstrap;

    private final PacketProcessor packetProcessor;

    public DreamClient(@NotNull PacketProcessor packetProcessor) {
        this.packetProcessor = packetProcessor;
    }

    public void init() {
        Check.stateCondition(initialized, "Netty server has already been initialized!");
        this.initialized = true;

        worker = new NioEventLoopGroup();

        bootstrap = new Bootstrap()
                .group(worker)
                .channel(NioServerSocketChannel.class);


        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(@NotNull SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast(DECODER_HANDLER_NAME, new PacketDecoder());

                pipeline.addLast(ENCODER_HANDLER_NAME, new PacketEncoder());

                pipeline.addLast(CLIENT_CHANNEL_NAME, new ClientHandler(packetProcessor));
            }
        });
    }

    private Channel serverChannel;

    public void start() {
        // Bind address
        try {
            ChannelFuture cf = bootstrap.connect("mc.dreamnw.ru", 8888).sync();

            if (!cf.isSuccess()) {
                throw new IllegalStateException("Unable connect to server");
            }else {
                System.out.println("Succes conected");
            }
            this.serverChannel = cf.channel();
            serverChannel.closeFuture().sync();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        try {
            this.serverChannel.close().sync();
        }catch (Exception e) {
            e.printStackTrace();
        }
        this.worker.shutdownGracefully();
    }

    public Channel getServerChannel() {
        return serverChannel;
    }
}
