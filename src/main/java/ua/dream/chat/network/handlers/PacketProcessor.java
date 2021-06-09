package ua.dream.chat.network.handlers;

import com.google.common.collect.Queues;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import ua.dream.chat.App;
import ua.dream.chat.network.netty.frame.Packet;
import ua.dream.chat.network.netty.frame.PacketOutHandler;
import ua.dream.chat.utils.async.AsyncUtils;

import java.util.Queue;

public class PacketProcessor {

    private final Queue<PacketContainer> packets = Queues.newConcurrentLinkedQueue();
    private final PacketOutHandler handler;

    public PacketProcessor(PacketOutHandler handler) {
        this.handler = handler;
        AsyncUtils.runAsync(() -> {
            while (true) {
                update();
                try {
                    synchronized (PacketProcessor.this) {
                        PacketProcessor.this.wait();
                    }
                } catch (InterruptedException e) {
                    if(!App.enable.get()) {
                        update();
                        e.printStackTrace();
                        return;
                    }
                }
            }
        });
    }

    public void process(Packet<PacketOutHandler> packet , ChannelHandlerContext context) {
        System.out.println("Handle packet " + packet);
        packets.add(new PacketContainer(packet , (SocketChannel) context.channel()));
        synchronized (this) {
            this.notify();
        }

    }

    public void update() {
        while (!packets.isEmpty()) {
            PacketContainer packet = packets.poll();
            if(packet.getContext() != null && packet.getContext().isActive()) {
                packet.getPacket().handle(handler, packet.getContext());
            }
        }
    }
}
