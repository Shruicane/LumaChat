package frontend.controller;

import frontend.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;

public class CreateAccountController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repeatPasswordField;

    @FXML
    private Label loginLabel;

    @FXML
    private Button createAccountButton;


    @FXML
    private void onClickLoginLabel(){
        //Wechsle View zu LoginView
        MainApp.showLoginScreen();
    }

    @FXML
    private void onClickCreateAccount(){
        //Wenn account erfolgreich erstellt, zeige login screen
        MainApp.showLoginScreen();
    }
}
