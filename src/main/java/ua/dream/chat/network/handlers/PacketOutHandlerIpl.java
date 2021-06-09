package ua.dream.chat.network.handlers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import ua.dream.chat.network.ClientState;
import ua.dream.chat.network.netty.frame.PacketOutHandler;
import ua.dream.chat.network.netty.packet.out.*;
import ua.dream.chat.window.controllers.MainInterfaceController;

public class PacketOutHandlerIpl implements PacketOutHandler {

    @Override
    public void handle(PacketOut3RegistrationFailed packet) {
        ClientState.setState(ClientState.AUTH);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration failed");
            alert.setHeaderText(packet.getReason());
            alert.showAndWait();
            return;
        });
    }

    @Override
    public void handle(PacketOut4LoginFailed packet) {
        ClientState.setState(ClientState.AUTH);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authorization failed");
            alert.setHeaderText(packet.getReason());
            alert.showAndWait();
            return;
        });
    }

    @Override
    public void handle(PacketOut5UserData packet) {
        ClientState.setState(ClientState.LOGIN_IN_CHAT);
    }

    @Override
    public void handle(PacketOut6UserLogout packet) {
        ClientState.setState(ClientState.AUTH);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authorization failed");
            alert.setHeaderText(packet.getReason());
            alert.showAndWait();
            return;
        });
    }

    @Override
    public void handle(PacketOut8Message packet) {
        MainInterfaceController.add(packet);
    }
}
