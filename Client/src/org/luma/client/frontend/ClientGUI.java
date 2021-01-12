package org.luma.client.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.luma.client.network.ClientMain;

import java.io.IOException;
import java.util.UUID;

public class ClientGUI extends Application {

    private static Stage primaryStage;
    private static Scene loginScene;
    private static Scene createAccountScene;
    private static Scene mainViewScene;

    public static Image sendImage;

    private static ClientMain client;

    public static void main(String[] args) {

        System.out.println(UUID.randomUUID());
        System.out.println(UUID.randomUUID());
        System.out.println(UUID.randomUUID());

        client = new ClientMain("localhost", 54321);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        ClientGUI.sendImage = new Image("https://www.iconfinder.com/icons/1564528/download/png/48");
        ClientGUI.mainViewScene = new Scene(FXMLLoader.load(getClass().getResource("/org/luma/client/frontend/resources/MainView.fxml")));
        ClientGUI.loginScene = new Scene(FXMLLoader.load(getClass().getResource("/org/luma/client/frontend/resources/LoginScreenView.fxml")));
        ClientGUI.createAccountScene = new Scene(FXMLLoader.load(getClass().getResource("/org/luma/client/frontend/resources/CreateAccountView.fxml")));
        ClientGUI.primaryStage = primaryStage;
        ClientGUI.primaryStage.setScene(ClientGUI.loginScene);
        ClientGUI.primaryStage.show();
    }

    public static void showMainScreen(ClientMain client){
        ClientGUI.primaryStage.setScene(ClientGUI.mainViewScene);
    }

    public static void showLoginScreen(){
        ClientGUI.primaryStage.setScene(ClientGUI.loginScene);
    }

    public static void showCreateAccountScreen(){
        ClientGUI.primaryStage.setScene(ClientGUI.createAccountScene);
    }

    public static ClientMain getClient() {
        return client;
    }
}
