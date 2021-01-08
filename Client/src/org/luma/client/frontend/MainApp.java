package org.luma.client.frontend;

import frontend.controller.LoginScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.luma.client.network.ClientMain;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MainApp extends Application {

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
        MainApp.sendImage = new Image("https://www.iconfinder.com/icons/1564528/download/png/48");
        MainApp.mainViewScene = new Scene(FXMLLoader.load(getClass().getResource("/MainView.fxml")));
        MainApp.loginScene = new Scene(FXMLLoader.load(getClass().getResource("/LoginScreenView.fxml")));
        MainApp.createAccountScene = new Scene(FXMLLoader.load(getClass().getResource("/CreateAccountView.fxml")));
        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage.setScene(MainApp.loginScene);
        MainApp.primaryStage.show();
    }

    public static void showMainScreen(ClientMain client){
        MainApp.primaryStage.setScene(MainApp.mainViewScene);
    }

    public static void showLoginScreen(){
        MainApp.primaryStage.setScene(MainApp.loginScene);
    }

    public static void showCreateAccountScreen(){
        MainApp.primaryStage.setScene(MainApp.createAccountScene);
    }

    public static ClientMain getClient() {
        return client;
    }
}
