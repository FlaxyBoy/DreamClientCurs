package ua.dream.chat;

import ua.dream.chat.network.Network;
import ua.dream.chat.network.handlers.PacketOutHandlerIpl;
import ua.dream.chat.network.handlers.PacketProcessor;
import ua.dream.chat.network.netty.frame.Packet;

import java.util.concurrent.atomic.AtomicBoolean;

public class App {

    public static final AtomicBoolean enable = new AtomicBoolean(true);
    private static Network client;
    private static PacketProcessor processor;

    public static void main() {
        processor = new PacketProcessor(new PacketOutHandlerIpl());
        client = new Network(processor);
    }

    public static Network getClient() {
        return client;
    }

    public static void sendPacket(Packet<?> packet) {
        client.getChannel().writeAndFlush(packet);
    }

    public static void disable() {
        enable.set(false);
        client.stop();
    }
}
