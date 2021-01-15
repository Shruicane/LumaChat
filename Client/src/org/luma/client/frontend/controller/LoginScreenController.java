package org.luma.client.frontend.controller;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.luma.client.frontend.ClientGUI;
import org.luma.client.network.ClientMain;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginScreenController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passWordTextField;

    @FXML
    private TextField ipTextField;

    @FXML
    private void onClickLogin() {
        ClientMain client = ClientGUI.getClient();
        if (client.login(userNameTextField.getText(), passWordTextField.getText())) {
            ClientGUI.showMainScreen(client);
            clear();
        } else {
            // Login Failed
            ClientGUI.getController().showPopup("Wrong Password or Username! (or you are banned)", "Is your Device connected to the Internet?");
            //System.out.println("Wrong pwd or something");
        }
    }

    @FXML
    private void onClickAccountLabel() {
        //Wechsle View zu CreateAccountView
        ClientGUI.showCreateAccountScreen();
        clear();
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
        passWordTextField.clear();
        userNameTextField.requestFocus();
    }
}
