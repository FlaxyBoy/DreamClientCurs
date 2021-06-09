package ua.dream.chat.window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ua.dream.chat.App;
import ua.dream.chat.network.ClientState;
import ua.dream.chat.window.frames.LoadFrame;

import java.io.IOException;

public class UserLogin extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        UserLogin.primaryStage = primaryStage;
        LoadFrame.init();
        primaryStage.setOnCloseRequest(event -> {
            App.disable();
        });
        ClientState.setState(ClientState.CONNECT);
        App.main();
    }

    public static void setWaitForm() {
        synchronized (UserLogin.class) {
            Parent root = LoadFrame.getRoot();
            primaryStage.setTitle("DreamChat");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root , 325 , 60));
            primaryStage.show();
        }
    }

    public static void setMainForm() throws IOException{
        synchronized (UserLogin.class) {
            Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("main_interface.fxml"));
            primaryStage.setTitle("DreamChat");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    public static void setLoginForm() throws IOException {
        synchronized (UserLogin.class) {
            Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("login_interface.fxml"));
            primaryStage.setTitle("DreamChat");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }

    public static void setRegisterForm() throws IOException {
        synchronized (UserLogin.class) {
            Parent root = FXMLLoader.load(ClassLoader.getSystemClassLoader().getResource("register_interface.fxml"));
            primaryStage.setTitle("DreamChat");
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }
}
