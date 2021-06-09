package ua.dream.chat.network;

import ua.dream.chat.network.netty.packet.out.PacketOut5UserData;

public class UserData {

    private static String login;
    private static String displayName;

    public static void handle(PacketOut5UserData packet) {
        login = packet.getLogin();
        displayName = packet.getDisplayName();
    }

    public static String getLogin() {
        return login;
    }

    public static String getDisplayName() {
        return displayName;
    }
}
