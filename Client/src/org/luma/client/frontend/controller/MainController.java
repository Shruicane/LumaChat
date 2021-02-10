package org.luma.client.frontend.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.luma.client.frontend.ClientGUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.luma.client.network.ClientMain;
import org.luma.client.network.Logger;

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

        if (chatTabs.getSelectionModel().getSelectedIndex() == 0) {
            if (!msg.isEmpty() && getSelectedChat() != null) {
                if(client.sendPrivate(getSelectedChat(), msg)) {
                    new Logger(ClientGUI.getClient().getGui()).message("private", getSelectedChat(), "Du: " + msg);
                }
            }
        } else {
            if (!msg.isEmpty() && getSelectedGroup() != null) {
                client.sendGroup(getSelectedGroup(), msg);
            }
        }
        msgTextArea.clear();

        //TODO: msg an Server senden - nur in der Textliste anzeigen wenn vom Server eine Bestätigung kommt
    }

    public void updateMessages(String type, String receiver, String msg) {
        if (type.equals("group")) {
            if (!groupMessages.containsKey(receiver))
                groupMessages.put(receiver, new ArrayList<>());
            groupMessages.get(receiver).add(msg);

            if (getSelectedGroup() != null && receiver.matches(getSelectedGroup()) && chatTabs.getSelectionModel().getSelectedIndex() == 1)
                messagesTextArea.appendText(msg + "\n");
        } else if (type.equals("private")) {
            if (!chatData.containsKey(receiver))
                chatData.put(receiver, new ArrayList<>());
            chatData.get(receiver).add(msg);

            if (getSelectedChat() != null && receiver.matches(getSelectedChat()) && chatTabs.getSelectionModel().getSelectedIndex() == 0)
                messagesTextArea.appendText(msg + "\n");
        }
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
                if (client.isConnected())
                    client.disconnect("Loggout");

                messagesTextArea.clear();
                chatData.clear();
                groupData.clear();
                groupMessages.clear();
                groupChats.getSelectionModel().clearSelection();
                privateChats.getSelectionModel().clearSelection();
                chatTabs.getSelectionModel().select(0);
                ClientGUI.showLoginScreen();
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    Map<String, ArrayList<String>> groupData;
    Map<String, ArrayList<String>> groupMessages = new HashMap<>();

    private String selectedGroup;

    public void updateGroupView(Map<String, ArrayList<String>> data) {
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                selectedGroup = groupChats.getSelectionModel().getSelectedItem();
                groupData = data;
                ObservableList<String> groups = groupChats.getItems();
                groups.clear();
                groups.addAll(groupData.keySet());

                groupChats.setItems(groups);

                groupChats.getSelectionModel().select(selectedGroup);
                selectedGroup = "";

                updateUserList();
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    Map<String, ArrayList<String>> chatData;

    private String selectedChat;

    public void updatePrivateView(Map<String, ArrayList<String>> data) {
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                selectedChat = privateChats.getSelectionModel().getSelectedItem();
                chatData = data;
                ObservableList<String> chats = privateChats.getItems();
                chats.clear();
                chats.addAll(chatData.keySet());

                privateChats.setItems(chats);

                privateChats.getSelectionModel().select(selectedChat);
                selectedChat = "";

                updateChat();
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void updateUserList() {
        try {
            messagesTextArea.clear();
        } catch (Exception ignored) {
        }
        if (getSelectedGroup() != null) {
            ObservableList<String> users = groupChatMembers.getItems();
            users.clear();
            users.addAll(groupData.get(getSelectedGroup()));

            groupChatMembers.setItems(users);

            if (groupMessages.containsKey(getSelectedGroup()))
                for (String msg : groupMessages.get(getSelectedGroup()))
                    messagesTextArea.appendText(msg + "\n");
        } else {
            ObservableList<String> users = groupChatMembers.getItems();
            users.clear();
            groupChatMembers.setItems(users);

            messagesTextArea.clear();
        }
    }

    @FXML
    private void updateChat() {
        try {
            messagesTextArea.clear();
        } catch (Exception ignored) {
        }
        if (getSelectedChat() != null) {
            if (chatData.containsKey(getSelectedChat()))
                for (String msg : chatData.get(getSelectedChat()))
                    messagesTextArea.appendText(msg + "\n");
        }
    }

    @FXML
    private void onSelectedPrivateChats() {
        updateChat();
    }

    @FXML
    private void onSelectedGroupChats() {
        updateUserList();
    }

    private String getSelectedGroup() {
        return groupChats.getSelectionModel().getSelectedItems().get(0);
    }

    private String getSelectedChat() {
        return privateChats.getSelectionModel().getSelectedItems().get(0);
    }
}
