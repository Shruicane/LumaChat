package org.luma.server.frontend.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.luma.server.database.*;
import org.luma.server.frontend.ServerGUI;
import org.luma.server.network.ClientManager;
import org.luma.server.network.Logger;
import org.luma.server.network.ServerMain;
import org.luma.server.settings.ServerSettings;
import org.luma.server.settings.Settings;

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
    private TableView<User> privateChatsTableView;

    @FXML
    private TableColumn<User, String> leftColumnChatsTableview;

    @FXML
    private TableColumn<User, Password> rightColumnChatsTableview;

    //</editor-fold>

    //<editor-fold desc="Private Chats Tab">

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
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Please Enter Username:");
            dialog.setTitle("Add User");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(username -> {
                ArrayList<String> allUsersLowerCase = new ArrayList<>();
                for(String s : groupManager.getAllUsers(getSelectedGroup().getName())){
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
                    for(String aUser:affectedUsers)
                        sendUpdateInfo(aUser, "group", userManager.getAllGroupsWithUser(aUser));
                    cm.message(getSelectedGroup().getName(), username, username + " has joined the Room!");

                }else{
                    Alert alert = new Alert(AlertType.WARNING, "User ist schon Mitglied der Gruppe oder existiert nicht!");
                    alert.showAndWait();
                }
            });
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
    //</editor-fold>

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
    }


    MySQLConnection mySQLConnection;
    UserManagement userManager;
    GroupManagement groupManager;
    IOManagement ioManager;
    ServerMain server;
    ClientManager cm;
    Logger log;


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
}
