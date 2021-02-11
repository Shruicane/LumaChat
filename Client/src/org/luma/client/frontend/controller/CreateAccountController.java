package org.luma.client.frontend.controller;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.luma.client.frontend.ClientGUI;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.luma.client.network.ClientMain;

public class CreateAccountController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private TextField ipTextField;


    @FXML
    private void onClickLoginLabel(){
        //Wechsle View zu LoginView
        ClientGUI.showLoginScreen();
        clear();
    }

    @FXML
    private void onClickCreateAccount(){

        if(!passwordField.getText().matches(repeatPasswordField.getText())){
            Alert alert = new Alert(AlertType.ERROR, "Passwörter stimmen nicht überein!");
            alert.showAndWait();
            this.passwordField.clear();
            this.repeatPasswordField.clear();
        } else {
            ClientMain client = new ClientMain("localhost", 54321, null);

            if(client.register(userNameTextField.getText(), passwordField.getText())){
                ClientGUI.showLoginScreen();
                client.disconnect("Account creation");
                clear();
            } else {
                //Register failed
                ClientGUI.getController().showPopup("This Name is already taken!", "Is your device connected to the Internet?");
            }
        }
    }

    @FXML
    private void changeIp() {
        boolean success = ClientGUI.getClient().changeIp(ipTextField.getText());
        if (success) {
            ipTextField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            ipTextField.setBackground(new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private void clear(){
        userNameTextField.clear();
        passwordField.clear();
        repeatPasswordField.clear();
        userNameTextField.requestFocus();
    }
}
