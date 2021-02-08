package org.luma.client.frontend.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.luma.client.frontend.ClientGUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.luma.client.network.ClientMain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class MainController {

    @FXML
    private ListView<String> privateChats;

    @FXML
    private ListView<String> groupChats;

    @FXML
    private ListView<String> groupChatMembers;

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

        ImageView btnImg = new ImageView(ClientGUI.sendImage);
        btnImg.setFitWidth(50);
        btnImg.setFitHeight(50);

        this.sendMsgBtn.setGraphic(btnImg);

        messagesTextArea.setFont(Font.font("Monospaced", FontWeight.MEDIUM, FontPosture.REGULAR, 15));

        chatTabs.getSelectionModel().selectedItemProperty().addListener(
                (ov, fromtTab, toTab) -> {
                    //TODO: Load Chats
                }
        );
        ClientGUI.setMainController(this);
    }

    @FXML
    private void onClickLogOut() {
        logout();
    }

    @FXML
    private void onClickChangePwd() {
        //TODO: Dialog zum Pwd ändern öffnen
        //TODO: in der Datenbank Pwd ändern
    }

    @FXML
    private void showOnlineList() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Warning test:");
        dialog.setTitle("Warn!");
        //dialog.setContentText("Your favourite int: ");

        Optional<String> result = dialog.showAndWait();
        //TODO: online liste abrufen und anzeigen

    }

    @FXML
    private void onClickSendMsg() {
        ClientMain client = ClientGUI.getClient();
        String msg = msgTextArea.getText();

        if (!msg.isEmpty() && getSelectedGroup() != null) {
            client.send(getSelectedGroup(), msg);
        }
        msgTextArea.clear();

        //TODO: msg an Server senden - nur in der Textliste anzeigen wenn vom Server eine Bestätigung kommt
    }

    public void updateMessages(String group, String msg) {
        if(!groupMessages.containsKey(group))
            groupMessages.put(group, new ArrayList<String>());
        groupMessages.get(group).add(msg);

        if(getSelectedGroup() != null && group.matches(getSelectedGroup()))
            messagesTextArea.appendText(msg + "\n");
    }

    @FXML
    public void showPopup(String msg, String hint) {
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, hint, ButtonType.OK);
                alert.setHeaderText(msg);
                alert.showAndWait();
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void showPopup(String msg) {
        showPopup(msg, "");
    }

    public void logout() {
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                ClientMain client = ClientGUI.getClient();
                if(client.isConnected())
                    client.disconnect("Loggout");

                messagesTextArea.clear();
                ClientGUI.showLoginScreen();
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    Map<String, ArrayList<String>> groupData;
    Map<String, ArrayList<String>> groupMessages = new HashMap<>();

    private int selected = 0;

    public void updateGroupView(Map<String, ArrayList<String>> data){
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                selected = groupChats.getSelectionModel().getSelectedIndex();
                groupData = data;
                ObservableList<String> groups = groupChats.getItems();
                groups.clear();
                groups.addAll(groupData.keySet());

                groupChats.setItems(groups);

                groupChats.getSelectionModel().select(selected);

                updateUserList();
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void updateUserList(){
        if(getSelectedGroup() != null){
            ObservableList<String> users = groupChatMembers.getItems();
            users.clear();
            users.addAll(groupData.get(getSelectedGroup()));

            groupChatMembers.setItems(users);

            messagesTextArea.clear();
            if(groupMessages.containsKey(getSelectedGroup()))
                for(String msg:groupMessages.get(getSelectedGroup()))
                    messagesTextArea.appendText(msg + "\n");
        } else{
            ObservableList<String> users = groupChatMembers.getItems();
            users.clear();
            groupChatMembers.setItems(users);

            messagesTextArea.clear();
        }
    }

    private String getSelectedGroup() {
        return groupChats.getSelectionModel().getSelectedItems().get(0);
    }
}
