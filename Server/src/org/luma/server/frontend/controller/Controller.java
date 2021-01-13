package org.luma.server.frontend.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.luma.server.database.*;
import org.luma.server.network.ClientManager;
import org.luma.server.network.ServerMain;
import org.luma.server.settings.ServerSettings;
import org.luma.server.settings.Settings;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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
    private Button permanentBanButton;

    @FXML
    private Button unbanButton;

    @FXML
    private ToggleButton showPwdButton;

    @FXML
    private ListView<Group> groupList;

    @FXML
    private ListView<String> userList;

    @FXML
    private Button removeUserButton;

    @FXML
    private Button addUserButton;

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

    //</editor-fold>

    //<editor-fold desc="User Management Tab">
    @FXML
    private void onClickWarnUserButton() {
        if (getSelectedUser() != null) {
            showPopup("Warn!", "Warning User: " + getSelectedUser().getName(), r -> cm.warn(getSelectedUser().getName(), r));
        }
    }

    @FXML
    private void onClickTempBanButton() {
        if (getSelectedUser() != null) {
            showPopup("Kick!", "Kicking User: " + getSelectedUser().getName(), r -> cm.kick(getSelectedUser().getName(), r, "You were Kicked!"));
        }
    }

    @FXML
    private void onClickPermanentBanButton() {
        if (getSelectedUser() != null) {
            if (cm.isBanned(getSelectedUser().getName())) {
                cm.unban(getSelectedUser().getName());
            } else {
                showPopup("Ban!", "Banning User: " + getSelectedUser().getName(), r -> cm.ban(getSelectedUser().getName(), r));
            }
            onClickTable();
        }
    }

    @FXML
    private void onClickUnbanButton() {

    }

    @FXML
    private void onClickShowPwdButton() {
        ObservableList<User> items = userTableView.getItems();

        for (int i = 0; i < items.size(); i++) {
            User user = items.get(i);
            user.passwordProperty().getValue().show(showPwdButton.isSelected());
            System.out.println(user.passwordProperty().getValue().toString());
            items.set(i, user);
        }

        userTableView.setItems(items);
    }

    @FXML
    private void onClickTable() {
        if (getSelectedUser() != null) {
            boolean isBanned = cm.isBanned(getSelectedUser().getName());
            permanentBanButton.setText(isBanned ? "Unban User" : "Ban User");
        }
    }

    private User getSelectedUser() {
        return userTableView.getSelectionModel().getSelectedItems().get(0);
    }

    private void showPopup(String title, String msg, Consumer<? super String> consumer) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(msg);
        dialog.setTitle(title);

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(consumer);
    }
    //</editor-fold>

    //<editor-fold desc="Group Management Tab">
    @FXML
    private void onClickRemoveUserButton() {

    }

    @FXML
    private void onClickAddUserButton() {

    }

    @FXML
    private void onClickDeleteGroupButton() {
        if(getSelectedGroup() != null){
            groupManager.deleteGroup(getSelectedGroup());
            ObservableList<Group> groups = groupList.getItems();
            groups.remove(getSelectedGroup());
            groupList.setItems(groups);
        }
    }

    @FXML
    private void onClickCreateGroupButton() {
        ObservableList<Group> groups = groupList.getItems();
        Group group = new Group(groupManager.createGroup());
        groups.add(group);

        groupList.setItems(groups);
    }

    private Group getSelectedGroup() {
        return groupList.getSelectionModel().getSelectedItems().get(0);
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
        server = new ServerMain(this, mySQLConnection);
        cm = server.getClientManager();

        logArea.setFont(Font.font("Monospaced", FontWeight.MEDIUM, FontPosture.REGULAR, 15));

        userTableUsername.setCellValueFactory(features -> features.getValue().usernameProperty());
        userTablePassword.setCellValueFactory(features -> features.getValue().passwordProperty());
    }

    MySQLConnection mySQLConnection;
    UserManagement userManager;
    GroupManagement groupManager;
    ServerMain server;
    ClientManager cm;

    public void updateLogArea(String log) {
        try {
            logArea.setText(logArea.getText() + log + "\n");
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
}
