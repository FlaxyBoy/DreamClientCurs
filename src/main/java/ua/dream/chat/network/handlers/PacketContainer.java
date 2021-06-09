package ua.dream.chat.network.handlers;

import io.netty.channel.socket.SocketChannel;
import ua.dream.chat.network.netty.frame.Packet;
import ua.dream.chat.network.netty.frame.PacketOutHandler;

public final class PacketContainer {

    private final Packet<PacketOutHandler> packet;
    private final SocketChannel context;

    protected PacketContainer(Packet<PacketOutHandler> packet , SocketChannel context) {
        this.packet = packet;
        this.context = context;
    }

    protected SocketChannel getContext() {
        return context;
    }

    protected Packet<PacketOutHandler> getPacket() {
        return packet;
    }
}
