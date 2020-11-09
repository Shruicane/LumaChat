package frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import settings.ServerSettings;
import settings.Settings;

public class Controller {


    //<editor-fold desc="FXML Bindings">

    @FXML
    private TextArea logArea;

    @FXML
    private TableView<String> userTableView;
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
    private ListView<String> groupList;

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
    private void onClickWarnUserButton(){

    }

    @FXML
    private void onClickTempBanButton(){

    }

    @FXML
    private void onClickPermanentBanButton(){

    }

    @FXML
    private void onClickUnbanButton(){

    }

    @FXML
    private void onClickShowPwdButton(){

    }
    //</editor-fold>

    //<editor-fold desc="Group Management Tab">
    @FXML
    private void onClickRemoveUserButton(){

    }

    @FXML
    private void onClickAddUserButton(){

    }

    @FXML
    private void onClickDeleteGroupButton(){

    }

    @FXML
    private void onClickCreateGroupButton(){

    }
    //</editor-fold>

    //<editor-fold desc="Settings Tab">
    @FXML
    private void onClickSaveBtn(){

        String contentText = "";
        if(! Settings.getIpAddress().equals(this.ipAddressTextField.getText())){
            contentText += "IP-Address: " + Settings.getIpAddress() + " -> " + this.ipAddressTextField.getText() + "\n";
        }
        if(! Settings.getPort().equals(this.portTextField.getText())){
            contentText += "Port: " + Settings.getPort() + " -> " + this.portTextField.getText() + "\n";
        }
        if(! Settings.getDatabase().equals(this.databaseTextField.getText())){
            contentText += "Database name: " + Settings.getDatabase() + " -> " + this.databaseTextField.getText() + "\n";
        }
        if(! Settings.getDatabaseUser().equals(this.dataBaseUserTextField.getText())){
            contentText += "Database-User: " + Settings.getDatabaseUser() + " -> " + this.dataBaseUserTextField.getText() + "\n";
        }
        if(! Settings.getDatabasePassword().equals(this.databasePasswordTextField.getText())){
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
    private void onClickDiscardBtn(){
        this.ipAddressTextField.setText(Settings.getIpAddress());
        this.portTextField.setText(Settings.getPort());
        this.databaseTextField.setText(Settings.getDatabase());
        this.dataBaseUserTextField.setText(Settings.getDatabaseUser());
        this.databasePasswordTextField.setText(Settings.getDatabasePassword());
    }
    //</editor-fold>

    @FXML
    private void initialize(){
        this.ipAddressTextField.setText(Settings.getIpAddress());
        this.portTextField.setText(Settings.getPort());
        this.databaseTextField.setText(Settings.getDatabase());
        this.dataBaseUserTextField.setText(Settings.getDatabaseUser());
        this.databasePasswordTextField.setText(Settings.getDatabasePassword());
    }

}
