package frontend.controller;

import org.luma.client.frontend.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.luma.client.network.ClientMain;


public class MainController {

    @FXML
    private ListView<String> privateChats;

    @FXML
    private ListView<String> groupChats;

    @FXML
    private TextArea msgTextArea;

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

        ImageView btnImg = new ImageView(MainApp.sendImage);
        btnImg.setFitWidth(50);
        btnImg.setFitHeight(50);

        this.sendMsgBtn.setGraphic(btnImg);

        chatTabs.getSelectionModel().selectedItemProperty().addListener(
                (ov, fromtTab, toTab) -> {
                    //TODO: Load Chats
                }
        );

    }

    @FXML
    private void onClickLogOut() {
        ClientMain client = MainApp.getClient();

        client.disconnect("Loggout");
        MainApp.showLoginScreen();
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
        ClientMain client = MainApp.getClient();
        String msg = this.msgTextArea.getText();

        if(!msg.isEmpty()){
            client.send(msg);
        }
        //TODO: msg an Server senden - nur in der Textliste anzeigen wenn vom Server eine Bestätigung kommt
    }


}
