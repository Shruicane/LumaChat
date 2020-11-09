package frontend.controller;

import frontend.MainApp;
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
        //Check Account Data -> showMainScreen
        MainApp.showMainScreen();
    }

    @FXML
    private void onClickAccountLabel(){
        //Wechsle View zu CreateAccountView
        MainApp.showCreateAccountScreen();
    }



}
