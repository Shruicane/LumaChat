package org.luma.server.frontend;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.luma.server.network.ServerMain;

import java.io.IOException;

public class ServerGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/luma/server/frontend/resources//Main.fxml"));
        Scene home = new Scene(root);
        primaryStage.setScene(home);
        primaryStage.show();
        //TODO: mysql datenbank nicht erreichbar -> warnung

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // TODO: stop the server
                System.err.println("Stopping the Server");
                System.exit(0);
            }
        });
    }
}
