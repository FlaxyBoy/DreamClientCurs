package ua.dream.chat.window.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ua.dream.chat.App;
import ua.dream.chat.network.UserData;
import ua.dream.chat.network.netty.packet.in.PacketIn7UserMessage;
import ua.dream.chat.network.netty.packet.out.PacketOut8Message;
import ua.dream.chat.utils.validate.CheckUserData;

import java.net.URL;
import java.util.ResourceBundle;

public class MainInterfaceController implements Initializable {


    private static MainInterfaceController controller;

    @FXML
    TextArea mainArena;

    @FXML
    TextField msgField;

    public static MainInterfaceController getController() {
        return controller;
    }

    public static void add(PacketOut8Message message){
        while (controller == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        controller.addMessage(message);

    }

    public void addMessage(PacketOut8Message message) {
        Platform.runLater(() -> {
            String login = message.getSender().equals(UserData.getLogin()) ? "You" :
                    message.getDisplayName().equals("") ?
                    message.getSender() : message.getDisplayName();
            if(message.getTypeMessage() == PacketOut8Message.Type.USER_MESSAGE) {
                String input = String.format("[%s]: %s\n" , login , message.getMessage());
                mainArena.appendText(input);
            }
            if(message.getTypeMessage() == PacketOut8Message.Type.PRIVATE_MESSAGE) {
                String input = String.format("[%s >> you]: %s\n" , login , message.getMessage());
                mainArena.appendText(input);
            }
            if(message.getTypeMessage() == PacketOut8Message.Type.SEND_PRIVATE_MESSAGE) {
                String input = String.format("[you >> %s]: %s\n" , login , message.getMessage());
                mainArena.appendText(input);
            }
            if(message.getTypeMessage() == PacketOut8Message.Type.SERVER_MESSAGE) {
                String input = String.format("[SERVER NOTIFY]: %s\n" , message.getMessage());
                mainArena.appendText(input);
            }
        });
    }


    public void onSend(ActionEvent actionEvent) {
        if(msgField.getText().equals("")) return;
        App.getClient().getChannel().writeAndFlush(new PacketIn7UserMessage(msgField.getText()));
        msgField.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        msgField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (msgField.getText().length() > CheckUserData.MAX_MESSAGE_LENGTH) {
                    String s = msgField.getText().substring(0, CheckUserData.MAX_MESSAGE_LENGTH);
                    msgField.setText(s);
                }
            }
        });
        controller = this;
    }
}
