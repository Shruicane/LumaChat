package frontend.controller;

import org.luma.client.frontend.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.luma.client.network.ClientMain;

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

        if(!passwordField.getText().matches(repeatPasswordField.getText())){
            System.out.println("Passwörter stimmen nicht überein!");
        } else {
            ClientMain client = new ClientMain("localhost", 54321);

            if(client.register(userNameTextField.getText(), passwordField.getText())){
                client.disconnect("Account Created");
                MainApp.showLoginScreen();
            } else {
                //Register failed
                client.disconnect("Name probably already exists");
            }
        }
    }
}
