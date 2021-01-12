package org.luma.client.frontend.controller;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.luma.client.frontend.ClientGUI1;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.luma.client.network.ClientMain;


public class MainController {

    @FXML
    private ListView<String> privateChats;

    @FXML
    private ListView<String> groupChats;

    @FXML
    private TextField msgTextArea;

    @FXML
    private Button sendMsgBtn;

    @FXML
    private TextArea messagesTextArea;

    @FXML
    private TabPane chatTabs;

    @FXML
    private void initialize() {
        //Chats laden
        privateChats.getItems().addAll("Chat1", "Chat2", "Chat3");

        ImageView btnImg = new ImageView(ClientGUI1.sendImage);
        btnImg.setFitWidth(50);
        btnImg.setFitHeight(50);

        this.sendMsgBtn.setGraphic(btnImg);

        messagesTextArea.setFont(Font.font("Monospaced", FontWeight.MEDIUM, FontPosture.REGULAR, 15));

        chatTabs.getSelectionModel().selectedItemProperty().addListener(
                (ov, fromtTab, toTab) -> {
                    //TODO: Load Chats
                }
        );
        ClientGUI1.setMainController(this);
    }

    @FXML
    private void onClickLogOut() {
        ClientMain client = ClientGUI1.getClient();

        client.disconnect("Loggout");
        ClientGUI1.showLoginScreen();
    }

    @FXML
    private void onClickChangePwd() {
        //TODO: Dialog zum Pwd ändern öffnen
        //TODO: in der Datenbank Pwd ändern
    }

    @FXML
    private void showOnlineList() {
        //TODO: online liste abrufen und anzeigen

    }

    @FXML
    private void onClickSendMsg() {
        ClientMain client = ClientGUI1.getClient();
        String msg = msgTextArea.getText();

        if(!msg.isEmpty()){
            client.send(msg);
        }
        msgTextArea.clear();

        //TODO: msg an Server senden - nur in der Textliste anzeigen wenn vom Server eine Bestätigung kommt
    }

    public void updateMessages(String msg){
        messagesTextArea.setText((messagesTextArea.getText() + msg + "\n"));
    }
}
