package ua.dream.chat.window.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ua.dream.chat.App;
import ua.dream.chat.network.ClientState;
import ua.dream.chat.network.netty.packet.in.PacketIn1Register;
import ua.dream.chat.utils.validate.CheckUserData;
import ua.dream.chat.window.UserLogin;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    public void onAction(ActionEvent actionEvent) {
        try {
            UserLogin.setLoginForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActionRegister(ActionEvent actionEvent) {
        if(ClientState.getState() == ClientState.WAIT_REGISTER_RESPONSE) return;
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
        if(!passwordField.getText().equals(passwordField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Password mismatch");
            alert.showAndWait();
            return;
        }
        if(!passwordField.getText().equals(confirmPasswordField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Confirm password wrong");
            alert.showAndWait();
            return;
        }
        App.sendPacket(new PacketIn1Register(loginField.getText() , passwordField.getText()));
        ClientState.setState(ClientState.WAIT_REGISTER_RESPONSE);
    }


}
