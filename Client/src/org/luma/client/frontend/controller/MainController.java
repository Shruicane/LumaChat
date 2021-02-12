package org.luma.client.frontend.controller;

import Objects.Update;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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

import java.io.FileNotFoundException;
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
    private Button deleteChatBtn;

    @FXML
    private Button openChatBtn;

    ArrayList<String> onlineClients = new ArrayList<>();



    @FXML
    private void initialize() {
        //Chats laden

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
    private void onClickPrivateChats(){
        String messages = "";
        if(this.chatData.get(this.privateChats.getSelectionModel().getSelectedItem()) != null){
            for(String s : this.chatData.get(this.privateChats.getSelectionModel().getSelectedItem())){
                messages += s + "\n";
            }
        }

        this.messagesTextArea.setText(messages);
    }

    @FXML
    private void onClickDeleteChat(){

        String selectedChat = this.privateChats.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("You are about to delete chat with " + selectedChat);
        alert.setContentText("Are you sure you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            //Remove Item from list
            ClientGUI.getClient().send(new Update(Update.PRIVATE_DELETE, ClientGUI.getClient().getName(), privateChats.getSelectionModel().getSelectedItem()));
            this.privateChats.getItems().remove(this.privateChats.getSelectionModel().getSelectedItem());
            this.messagesTextArea.clear();
        }
    }

    @FXML
    private void onClickOpenChat(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Selection Dialog");
        alert.setHeaderText("Please select someone to chat with!");


        ListView<String> onlineList = new ListView<>();
        onlineList.getItems().addAll(onlineClients);
        onlineList.getItems().remove(ClientGUI.getClient().getName());

        for(String s : this.privateChats.getItems()){
            onlineList.getItems().remove(s);
        }

        onlineList.setMaxWidth(Double.MAX_VALUE);
        onlineList.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(onlineList, Priority.ALWAYS);
        GridPane.setHgrow(onlineList, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(onlineList, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setContent(expContent);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            if(onlineList.getSelectionModel().getSelectedItem() == null){
                Alert alreadyAdded = new Alert(Alert.AlertType.WARNING, "No Person selected!");
                alreadyAdded.showAndWait();
                return;
            }

            if(! this.privateChats.getItems().contains(onlineList.getSelectionModel().getSelectedItem())){
                this.privateChats.getItems().add(onlineList.getSelectionModel().getSelectedItem());
                ClientGUI.getClient().send(new Update(Update.PRIVATE_CREATE, ClientGUI.getClient().getName(), onlineList.getSelectionModel().getSelectedItem()));
            }else{
                Alert alreadyAdded = new Alert(Alert.AlertType.ERROR, "This Person is already in your list of private Chats!");
                alreadyAdded.showAndWait();
            }
        }

    }

    @FXML
    private void showOnlineList() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("List Dialog");
        alert.setHeaderText("See who's online!");


        ListView<String> onlineList = new ListView<>();
        onlineList.getItems().addAll(onlineClients);


        onlineList.setMaxWidth(Double.MAX_VALUE);
        onlineList.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(onlineList, Priority.ALWAYS);
        GridPane.setHgrow(onlineList, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(onlineList, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setContent(expContent);
        alert.showAndWait();

    }

    @FXML
    private void onClickSendMsg() {
        ClientMain client = ClientGUI.getClient();
        String msg = msgTextArea.getText();

        if (chatTabs.getSelectionModel().getSelectedIndex() == 0) {
            if (!msg.isEmpty() && getSelectedChat() != null) {
                if(client.sendPrivate(getSelectedChat(), msg)) {
                    new Logger(ClientGUI.getClient().getGui()).message("private", getSelectedChat(), ClientGUI.getClient().getName() + ": " + msg);
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
                updateUserList();
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

                for(String group:groupMessages.keySet()){
                    if(!groupData.containsKey(group))
                        groupMessages.remove(group);
                }

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


    public void updateOnlineClients(ArrayList<String> onlineClients){
        this.onlineClients = onlineClients;
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
