package ua.dream.chat.window.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ua.dream.chat.App;
import ua.dream.chat.network.ClientState;
import ua.dream.chat.network.netty.packet.in.PacketIn2Login;
import ua.dream.chat.utils.validate.CheckUserData;
import ua.dream.chat.window.UserLogin;

public class LoginController {

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    public void onAction(ActionEvent actionEvent) {
        if(ClientState.getState() == ClientState.WAIT_AUTH_RESPONSE) return;
        try {
            CheckUserData.checkUserName(loginField.getText());
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            return;
        }
        try {
            CheckUserData.checkUserPassword(passwordField.getText());
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
            return;
        }
        App.sendPacket(new PacketIn2Login(loginField.getText() , passwordField.getText()));
        ClientState.setState(ClientState.WAIT_AUTH_RESPONSE);
    }

    public void onActionRegister(ActionEvent actionEvent) {

        try {
            UserLogin.setRegisterForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
