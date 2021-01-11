package org.luma.server.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontendApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/luma/server/frontend/resources/Main.fxml"));
        Scene home = new Scene(root);
        primaryStage.setScene(home);
        primaryStage.show();
    }
}
