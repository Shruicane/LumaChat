package org.luma.client.frontend.controller;

import org.luma.client.frontend.ClientGUI;
import org.luma.client.network.ClientMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginScreenController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passWordTextField;

    @FXML
    private Label createAccountLabel;

    @FXML
    private Button loginButton;

    @FXML
    private void onClickLogin(){
        ClientMain client = ClientGUI.getClient();
        if(client.login(userNameTextField.getText(), passWordTextField.getText())) {
            ClientGUI.showMainScreen(client);
        } else {
            // Login Failed
            System.out.println("Wrong pwd or something");
        }
    }

    @FXML
    private void onClickAccountLabel(){
        //Wechsle View zu CreateAccountView
        ClientGUI.showCreateAccountScreen();
    }

}
