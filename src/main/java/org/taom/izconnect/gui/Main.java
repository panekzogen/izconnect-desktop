package org.taom.izconnect.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.taom.izconnect.gui.alljoyn.FXNetworkService;
import org.taom.izconnect.network.AbstractNetworkService;

public class Main extends Application {
    private FXNetworkService networkService;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("sample.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1024, 640));
        primaryStage.show();

        networkService = new FXNetworkService(fxmlLoader.getController());
        networkService.doConnect();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        networkService.doDisconnect();
    }
}
