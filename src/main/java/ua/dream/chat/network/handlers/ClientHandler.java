package ua.dream.chat.network.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ua.dream.chat.network.ClientState;
import ua.dream.chat.network.netty.frame.Packet;
import ua.dream.chat.network.netty.frame.PacketOutHandler;

public final class ClientHandler extends ChannelInboundHandlerAdapter {

    private final PacketProcessor processor;

    public ClientHandler(PacketProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ClientState.setState(ClientState.AUTH);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packet<PacketOutHandler> packet = (Packet<PacketOutHandler>) msg;
        processor.process(packet , ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ClientState.setState(ClientState.CONNECT);
    }
}
