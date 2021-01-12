package org.luma.client.frontend.controller;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.luma.client.frontend.ClientGUI1;
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
    private TextField ipTextField;

    @FXML
    private void onClickLogin() {
        ClientMain client = ClientGUI1.getClient();
        if (client.login(userNameTextField.getText(), passWordTextField.getText())) {
            ClientGUI1.showMainScreen(client);
        } else {
            // Login Failed
            System.out.println("Wrong pwd or something");
        }
    }

    @FXML
    private void onClickAccountLabel() {
        //Wechsle View zu CreateAccountView
        ClientGUI1.showCreateAccountScreen();
    }

    @FXML
    private void changeIp() {
        boolean success = ClientGUI1.getClient().changeIp(ipTextField.getText());
        if (success) {
            ipTextField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            ipTextField.setBackground(new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }
}
