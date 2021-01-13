package org.luma.client.frontend;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.luma.client.frontend.controller.MainController;
import org.luma.client.network.ClientMain;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class ClientGUI1 extends Application implements GUI{

    private static Stage primaryStage;
    private static Scene loginScene;
    private static Scene createAccountScene;
    private static Scene mainViewScene;

    public static Image sendImage;

    private static ClientMain client;

    private static MainController controller;

    public static void main(String[] args) {

        System.out.println(UUID.randomUUID());
        System.out.println(UUID.randomUUID());
        System.out.println(UUID.randomUUID());

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        ClientGUI1.sendImage = new Image("https://www.iconfinder.com/icons/1564528/download/png/48");
        ClientGUI1.mainViewScene = new Scene(FXMLLoader.load(getClass().getResource("/org/luma/client/frontend/resources/MainView.fxml")));
        ClientGUI1.loginScene = new Scene(FXMLLoader.load(getClass().getResource("/org/luma/client/frontend/resources/LoginScreenView.fxml")));
        ClientGUI1.createAccountScene = new Scene(FXMLLoader.load(getClass().getResource("/org/luma/client/frontend/resources/CreateAccountView.fxml")));
        ClientGUI1.primaryStage = primaryStage;
        ClientGUI1.primaryStage.setScene(ClientGUI1.loginScene);
        ClientGUI1.primaryStage.show();

        client = new ClientMain("localhost", 54321, this);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                client.stop();
            }
        });
    }

    public static void showMainScreen(ClientMain client){
        ClientGUI1.primaryStage.setScene(ClientGUI1.mainViewScene);
    }

    public static void showLoginScreen(){
        ClientGUI1.primaryStage.setScene(ClientGUI1.loginScene);
    }

    public static void showCreateAccountScreen(){
        ClientGUI1.primaryStage.setScene(ClientGUI1.createAccountScene);
    }

    public static ClientMain getClient() {
        return client;
    }
    public static MainController getController(){
        return controller;
    }
    public static void setMainController(MainController contr){
        controller = contr;
    }

    @Override
    public void updateMessages(String msg) {
        controller.updateMessages(msg);
    }

    public void showPopup(String msg, String hint) {
        controller.showPopup(msg, hint);
    }
    public void showPopup(String msg) {
        showPopup(msg, "");
    }

    @Override
    public void logout() {
        if(primaryStage.getScene() == ClientGUI1.mainViewScene)
            controller.logout();
    }
}
