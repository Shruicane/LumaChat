package frontend.controller;

import org.luma.client.frontend.MainApp;
import org.luma.client.network.ClientMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

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
        ClientMain client = MainApp.getClient();
        if(client.login(userNameTextField.getText(), passWordTextField.getText())) {
            MainApp.showMainScreen(client);
        } else {
            // Login Failed
            System.out.println("Wrong pwd or something");
        }
    }

    @FXML
    private void onClickAccountLabel(){
        //Wechsle View zu CreateAccountView
        MainApp.showCreateAccountScreen();
    }

}
