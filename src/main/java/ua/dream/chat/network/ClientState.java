package ua.dream.chat.network;

import javafx.application.Platform;
import ua.dream.chat.App;
import ua.dream.chat.window.UserLogin;
import ua.dream.chat.window.frames.LoadFrame;

import java.io.IOException;

public enum ClientState {
    CONNECT,
    AUTH,
    WAIT_AUTH_RESPONSE,
    WAIT_REGISTER_RESPONSE,
    LOGIN_IN_CHAT;

    private static ClientState state = CONNECT;

    ClientState() {

    }

    public static ClientState getState() {
        return state;
    }

    public static void setState(ClientState state) {
        if(!App.enable.get()) return;
        ClientState.state = state;
        switch (state) {
            case CONNECT:
                LoadFrame.setReason("Connect to server");
                Platform.runLater(UserLogin::setWaitForm);
                break;
            case AUTH:
                Platform.runLater(() -> {
                    try {
                        UserLogin.setLoginForm();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case WAIT_AUTH_RESPONSE:
                Platform.runLater(() -> {
                    LoadFrame.setReason("User authentication");
                    UserLogin.setWaitForm();
                });
                break;
            case WAIT_REGISTER_RESPONSE:
                Platform.runLater(() -> {
                    LoadFrame.setReason("User registration");
                    UserLogin.setWaitForm();
                });
                break;
            case LOGIN_IN_CHAT:
                Platform.runLater(() -> {
                    try {
                        UserLogin.setMainForm();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;

        }
    }
}
