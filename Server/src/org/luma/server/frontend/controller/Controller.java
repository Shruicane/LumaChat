package org.luma.server.frontend.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import org.luma.server.database.*;
import org.luma.server.frontend.ServerGUI;
import org.luma.server.network.ClientManager;
import org.luma.server.network.Logger;
import org.luma.server.network.ServerMain;
import org.luma.server.settings.ServerSettings;
import org.luma.server.settings.Settings;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class Controller {


    //<editor-fold desc="FXML Bindings">

    @FXML
    private TextArea logArea;

    @FXML
    private TableView<User> userTableView;
    @FXML
    private TableColumn<User, String> userTableUsername;
    @FXML
    private TableColumn<User, Password> userTablePassword;

    @FXML
    private Button warnUserButton;

    @FXML
    private Button tempBanButton;

    @FXML
    private ToggleButton permanentBanButton;

    @FXML
    private ToggleButton showPwdButton;

    @FXML
    private ListView<String> groupList;

    @FXML
    private ListView<String> userList;

    @FXML
    private Button removeUserButton;

    @FXML
    private Button addUserButton;

    @FXML
    private Button editGroupButton;

    @FXML
    private Button deleteGroupButton;

    @FXML
    private Button createGroupButton;

    @FXML
    private TextField ipAddressTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private TextField databaseTextField;

    @FXML
    private TextField dataBaseUserTextField;

    @FXML
    private TextField databasePasswordTextField;

    @FXML
    private Button saveSettingsBtn;

    @FXML
    private Button discardSettingsBtn;

    @FXML
    private Button createChatBtn;

    @FXML
    private Button deleteChatBtn;

   @FXML
   private ListView<String> privateChatsUser;

    @FXML
    private ListView<String> privateChatsUsersChats;

    //</editor-fold>

    //<editor-fold desc="Private Chats Tab">

    private String selectedUser;
    private String selectedChat;

    @FXML
    private void onClickDeleteChat(){
        if(selectedUser != null && selectedChat != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("You are about to delete " + this.selectedUser + "'s private Chat with " + this.selectedChat);
            alert.setContentText("Do you wish to continue?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                groupManager.deletePrivate(this.selectedUser, this.selectedChat);
                this.reloadTabs();
            }
        }
    }

    @FXML
    private void onClickCreateChat(){
        if(selectedUser != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("You are about to create a chat for " + this.selectedUser);
            alert.setContentText("Do you wish to continue?");
            TextField userToAdd = new TextField();
            alert.getDialogPane().setContent(userToAdd);
            userToAdd.setMaxWidth(75);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if(userToAdd.getText() != null && ! userToAdd.getText().equals("")){

                    if(this.privateChatsUser.getItems().contains(userToAdd.getText())){
                        groupManager.createPrivate(this.selectedUser, userToAdd.getText());
                    }else{
                        Alert warn = new Alert(AlertType.ERROR, "This user does not exist!");
                        warn.showAndWait();
                    }
                }else{
                    Alert warn = new Alert(AlertType.WARNING, "Please enter a name!");
                    warn.showAndWait();
                }

            }
        }
    }

    @FXML
    private void onClickChats(){
        this.selectedChat = this.privateChatsUsersChats.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void onClickUsers(){
        this.selectedUser = this.privateChatsUser.getSelectionModel().getSelectedItem();
        this.privateChatsUsersChats.getItems().clear();
        if(this.selectedUser != null) {
            for (Map.Entry<String, ArrayList<String>> entry : userManager.getAllChatsFromUser(this.selectedUser).entrySet()) {
                this.privateChatsUsersChats.getItems().add(entry.getKey());
            }
        }
    }

    //</editor-fold>

    //<editor-fold desc="User Management Tab">
    @FXML
    private void onClickWarnUserButton() {
        if (getSelectedUser() != null) {
            showPopup("Warn!", "Warning User: " + getSelectedUser().getName(), r -> {
                cm.warn(getSelectedUser().getName(), r);
                log.administration("Warned >> " + getSelectedUser().getName() + ": " + r);
            });
        }
    }

    @FXML
    private void onClickTempBanButton() {
        if (getSelectedUser() != null) {
            showPopup("Kick!", "Kicking User: " + getSelectedUser().getName(), r -> {
                if(cm.kick(getSelectedUser().getName(), r, "You were Kicked!"))
                    log.administration("Kicked >> " + getSelectedUser().getName() + ": " + r);
            });
        }
    }

    @FXML
    private void onClickPermanentBanButton() {
        if (getSelectedUser() != null) {
            if (cm.isBanned(getSelectedUser().getName())) {
                cm.unban(getSelectedUser().getName());
            } else {
                showPopup("Ban!", "Banning User: " + getSelectedUser().getName(), r -> {
                    cm.ban(getSelectedUser().getName(), r);
                    log.administration("Banned >> " + getSelectedUser().getName() + ": " + r);
                });
            }
            onClickUserTable();

        }
    }

    @FXML
    private void onClickShowPwdButton() {
        ObservableList<User> items = userTableView.getItems();

        for (int i = 0; i < items.size(); i++) {
            User user = items.get(i);
            user.passwordProperty().getValue().show(showPwdButton.isSelected());
            //System.out.println(user.passwordProperty().getValue().toString());
            items.set(i, user);
        }

        userTableView.setItems(items);
        if(showPwdButton.getText().equals("Show Passwords")){
            this.showPwdButton.setText("Hide Passwords");
        }else{
            this.showPwdButton.setText("Show Passwords");
        }
    }

    @FXML
    private void onClickUserTable() {
        if (getSelectedUser() != null) {
            boolean isBanned = cm.isBanned(getSelectedUser().getName());
            permanentBanButton.setSelected(isBanned);
        }
    }

    private User getSelectedUser() {
        return userTableView.getSelectionModel().getSelectedItems().get(0);
    }
    //</editor-fold>

    //<editor-fold desc="Group Management Tab">
    @FXML
    private void onClickRemoveUserButton() {
        if (getSelectedGroup() != null && getSelectedGroupUser() != null) {
            String username = getSelectedGroupUser();
            Group group = getSelectedGroup();

            ArrayList<String> affectedUsers = groupManager.getAllUsers(getSelectedGroup().getName());
            groupManager.removeUser(group, username);
            ObservableList<String> user = userList.getItems();
            user.remove(username);

            userList.setItems(user);
            log.administration("Removed User >> " + group + " -> " + username);

            updateListUser();


            for(String aUser:affectedUsers)
                sendUpdateInfo(aUser, "group", userManager.getAllGroupsWithUser(aUser));
            cm.message(group.getName(), username, username + " has left the Room!");

        }
    }

    @FXML
    private void onClickAddUserButton() {
        if (getSelectedGroup() != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Selection Dialog");
            alert.setHeaderText("Please select someone to add!");


            ListView<String> onlineList = new ListView<>();
            onlineList.getItems().addAll(userManager.getAllUsers());

            for(String s : this.userList.getItems()){
                onlineList.getItems().remove(s);
            }

            onlineList.setMaxWidth(Double.MAX_VALUE);
            onlineList.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(onlineList, Priority.ALWAYS);
            GridPane.setHgrow(onlineList, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(onlineList, 0, 1);

            alert.getDialogPane().setContent(expContent);

            Optional<ButtonType> result1 = alert.showAndWait();
            if (result1.get() == ButtonType.OK) {

                String username = onlineList.getSelectionModel().getSelectedItem();

                if(username != null && ! username.equals("")) {
                    ArrayList<String> allUsersLowerCase = new ArrayList<>();
                    for (String s : groupManager.getAllUsers(getSelectedGroup().getName())) {
                        allUsersLowerCase.add(s.toLowerCase());
                    }
                    if (userManager.userExists(username) && !allUsersLowerCase.contains(username.toLowerCase())) {
                        groupManager.addUser(getSelectedGroup(), username);
                        ObservableList<String> user = userList.getItems();
                        user.add(username);

                        userList.setItems(user);
                        log.administration("Added User >> " + getSelectedGroup() + " <- " + username);

                        updateListUser();

                        ArrayList<String> affectedUsers = groupManager.getAllUsers(getSelectedGroup().getName());
                        for (String aUser : affectedUsers)
                            sendUpdateInfo(aUser, "group", userManager.getAllGroupsWithUser(aUser));
                        cm.message(getSelectedGroup().getName(), username, username + " has joined the Room!");

                    } else {
                        Alert warn = new Alert(AlertType.WARNING, "User ist schon Mitglied der Gruppe oder existiert nicht!");
                        warn.showAndWait();
                    }
                }else{
                    Alert warn = new Alert(AlertType.WARNING, "Kein User wurde ausgewählt!");
                    warn.showAndWait();
                }
            }
        }

        // TODO: maybe add drop down menu to dialog
    }

    @FXML
    private void onClickDeleteGroupButton() {
        if (getSelectedGroup() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to delete this Group?", ButtonType.YES, ButtonType.NO);

            ButtonType result = alert.showAndWait().orElse(ButtonType.NO);

            if (ButtonType.YES.equals(result)) {
                Group group = getSelectedGroup();
                ArrayList<String> affectedUsers = groupManager.getAllUsers(group.getName());
                groupManager.deleteGroup(group);
                ObservableList<String> groups = groupList.getItems();
                groups.remove(group.getName());

                groupList.setItems(groups);
                log.administration("Deleted Group >> " + group);

                updateListGroup();
                for(String user:affectedUsers)
                    sendUpdateInfo(user, "group", userManager.getAllGroupsWithUser(user));

            }
        }
    }

    @FXML
    private void onClickCreateGroupButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Please neter Group Name:");
        dialog.setTitle("Create Group");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(r -> {
            if (groupList.getItems().filtered(group -> group.equals(r)).size() == 0) {
                ObservableList<String> groups = groupList.getItems();
                Group group = new Group(groupManager.createGroup(r));
                groups.add(group.getName());

                groupList.setItems(groups);

                log.administration("Created Group >> " + r);
            } else {
                dialog.close();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("This Name already exists!");
                alert.showAndWait();
            }

        });
    }

    @FXML
    private void onClickEditGroupButton() {
        if(getSelectedGroup() != null){



            showPopup("Change Name", "Please Enter new Name:", name -> {

                if(groupManager.groupExists(name)){
                    Alert warn = new Alert(AlertType.ERROR, "Groupname is already taken!");
                    warn.showAndWait();
                    return;
                }

                log.administration("Edited Group >> " + getSelectedGroup() + " -> " + name);
                groupManager.changeName(getSelectedGroup().getName(), name);

                groupList.getItems().clear();
                ObservableList<String> groupTableView = groupList.getItems();
                ArrayList<String> groups = groupManager.getAllGroups();
                groupTableView.addAll(groups);
                groupList.setItems(groupTableView);

                getSelectedGroup().setName(name);
                groupList.refresh();
            });

        }
    }

    @FXML
    private void updateListGroup() {
        if (getSelectedGroup() != null) {
            ObservableList<String> user = userList.getItems();
            if (user == null) {
                user = emptyDummyList;
            }
            user.clear();
            String name = getSelectedGroup().getName();
            user.addAll(groupManager.getAllUsers(name));
            userList.setItems(user);

            addUserButton.setVisible(true);
            removeUserButton.setVisible(getSelectedGroupUser() != null);
        } else {
            ObservableList<String> user = null;
            userList.setItems(user);

            addUserButton.setVisible(false);
            removeUserButton.setVisible(false);
        }
    }

    @FXML
    private void updateListUser() {
        removeUserButton.setVisible(getSelectedGroupUser() != null);
    }

    private Group getSelectedGroup() {
        return new Group(groupList.getSelectionModel().getSelectedItems().get(0));
    }

    private String getSelectedGroupUser() {
        return userList.getSelectionModel().getSelectedItems().get(0);
    }
    //</editor-fold>

    //<editor-fold desc="Settings Tab">
    @FXML
    private void onClickSaveBtn() {

        String contentText = "";
        if (!Settings.getIpAddress().equals(this.ipAddressTextField.getText())) {
            contentText += "IP-Address: " + Settings.getIpAddress() + " -> " + this.ipAddressTextField.getText() + "\n";
        }
        if (!Settings.getPort().equals(this.portTextField.getText())) {
            contentText += "Port: " + Settings.getPort() + " -> " + this.portTextField.getText() + "\n";
        }
        if (!Settings.getDatabase().equals(this.databaseTextField.getText())) {
            contentText += "Database name: " + Settings.getDatabase() + " -> " + this.databaseTextField.getText() + "\n";
        }
        if (!Settings.getDatabaseUser().equals(this.dataBaseUserTextField.getText())) {
            contentText += "Database-User: " + Settings.getDatabaseUser() + " -> " + this.dataBaseUserTextField.getText() + "\n";
        }
        if (!Settings.getDatabasePassword().equals(this.databasePasswordTextField.getText())) {
            contentText += "Database-Password: " + Settings.getDatabasePassword() + " -> " + this.databasePasswordTextField.getText() + "\n";
        }
        Settings.saveSettings(new ServerSettings(
                this.ipAddressTextField.getText(), this.portTextField.getText(), this.databaseTextField.getText(),
                this.dataBaseUserTextField.getText(), this.databasePasswordTextField.getText()));

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(contentText);
        alert.setHeaderText("Settings saved.");
        alert.showAndWait();

        log.administration("Settings >> Saved");
    }

    @FXML
    private void onClickDiscardBtn() {

        this.ipAddressTextField.setText(Settings.getIpAddress());
        this.portTextField.setText(Settings.getPort());
        this.databaseTextField.setText(Settings.getDatabase());
        this.dataBaseUserTextField.setText(Settings.getDatabaseUser());
        this.databasePasswordTextField.setText(Settings.getDatabasePassword());
    }

    @FXML
    private void onClickExportLogs(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Print Logs");
        alert.setHeaderText("Please enter the amount of Logs you want to print.");

        TextField amountTextfield = new TextField();
        TextField dirTextfield = new TextField();
        Button btn = new Button("...");
        btn.setPrefSize(25, 25);
        Label dirLabel = new Label("Please select a directory.");
        dirLabel.setPrefWidth(200);
        Label amountLabel = new Label("Please enter the amount of Logs you want to print.");
        DirectoryChooser dirChooser = new DirectoryChooser();
        btn.setOnMouseClicked(event -> {

            File selectedFile = dirChooser.showDialog(this.databaseTextField.getParent().getScene().getWindow());
            if(selectedFile != null) {
                dirTextfield.setText(selectedFile.getAbsolutePath());
            }
        });

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(amountLabel, 0, 0);
        expContent.add(amountTextfield, 0, 1);
        expContent.add(dirLabel, 0, 3);
        expContent.add(dirTextfield, 0, 4);
        expContent.add(btn, 1, 4);

        alert.getDialogPane().setContent(expContent);
        alert.showAndWait();

        try{
            int amount = Integer.parseInt(amountTextfield.getText());
            String path = dirTextfield.getText();
            exportLogs(path, amount);
            Alert al = new Alert(AlertType.INFORMATION, "Export Successfully");
            al.show();

        }catch (NumberFormatException e){
            Alert err = new Alert(AlertType.ERROR, "Please enter a valid number");
            err.showAndWait();
        }
    }
    //</editor-fold>



    @FXML
    public void reloadTabs(){

        userTableView.getItems().clear();
        userTablePassword.getColumns().clear();
        userTableUsername.getColumns().clear();

        emptyDummyList = userList.getItems();
        privateChatsUser.getItems().clear();
        privateChatsUsersChats.getItems().clear();
        groupList.getItems().clear();
        userList.getItems().clear();
        initComponents();
    }

    @FXML
    private void initialize() {
        this.ipAddressTextField.setText(Settings.getIpAddress());
        this.portTextField.setText(Settings.getPort());
        this.databaseTextField.setText(Settings.getDatabase());
        this.dataBaseUserTextField.setText(Settings.getDatabaseUser());
        this.databasePasswordTextField.setText(Settings.getDatabasePassword());

        mySQLConnection = new MySQLConnection(this);
        userManager = mySQLConnection.getUserManager();
        groupManager = mySQLConnection.getGroupManager();
        ioManager = new IOManagement();
        server = new ServerMain(this, ioManager, mySQLConnection);
        cm = server.getClientManager();
        log = server.getLogger();
        logManager = new LogManager(mySQLConnection.getMysqlDatabase());

        logArea.setFont(Font.font("Monospaced", FontWeight.MEDIUM, FontPosture.REGULAR, 15));

        userTableUsername.setCellValueFactory(features -> features.getValue().usernameProperty());
        userTablePassword.setCellValueFactory(features -> features.getValue().passwordProperty());

        emptyDummyList = userList.getItems();

        initComponents();
    }

    private void initComponents(){
        //Test Connection
        if(! groupManager.mySQLDataBase.openConnection()) {
            System.out.println("Database not reachable! Application shutting down.");
            System.exit(0);
        }
        groupManager.mySQLDataBase.closeConnection();


        //User Tableview
        ObservableList<User> userList = userTableView.getItems();
        Map<String, String> user = groupManager.getUsers();
        for(Map.Entry<String, String> entry:user.entrySet())
            userList.add(new User(entry.getKey(), entry.getValue(), false, false));
        userTableView.setItems(userList);

        //Group Listview
        ObservableList<String> groupTableView = groupList.getItems();
        ArrayList<String> groups = groupManager.getAllGroups();
        groupTableView.addAll(groups);
        groupList.setItems(groupTableView);

        //Private Chats
        this.privateChatsUser.getItems().addAll(userManager.getAllUsers());
    }


    MySQLConnection mySQLConnection;
    UserManagement userManager;
    GroupManagement groupManager;
    IOManagement ioManager;
    ServerMain server;
    ClientManager cm;
    Logger log;
    LogManager logManager;


    private ObservableList<String> emptyDummyList;

    private void showPopup(String title, String msg, Consumer<? super String> consumer) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(msg);
        dialog.setTitle(title);

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(consumer);
    }

    public void updateLogArea(String log) {
        try {
            logArea.appendText(log + "\n");
        } catch (Exception e) {
            System.out.println("ERROR: org/luma/server/frontend/controller/Controller.java -> updateLogArea()");
            e.printStackTrace();
        }
    }

    public void updateUser(String username, String password, boolean online) {
        ObservableList<User> items = userTableView.getItems();
        items.add(new User(username, password, online, showPwdButton.isSelected()));
        userTableView.setItems(items);
    }

    public void sendUpdateInfo(String username, String type, Object data){
        cm.sendUpdateInfo(username, type, data);
    }

    public void exportLogs(String path, int count) {
        ioManager.writeLog(path + "\\logs.txt", logManager.getLogs(count));
    }
}
