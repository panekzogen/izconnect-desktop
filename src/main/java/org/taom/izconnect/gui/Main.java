package org.taom.izconnect.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.taom.izconnect.gui.alljoyn.FXNetworkService;
import org.taom.izconnect.gui.alljoyn.PCServiceImpl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private boolean firstTime;
    private TrayIcon trayIcon;

    private FXNetworkService networkService;
    private PCServiceImpl pcService;

    @Override
    public void start(Stage primaryStage) throws Exception{
        createTrayIcon(primaryStage);
        firstTime = true;
        Platform.setImplicitExit(false);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("IZConnect Desktop Application");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.getIcons().add(new javafx.scene.image.Image("graphics/trayicon.png"));
        primaryStage.show();

        pcService = new PCServiceImpl();
        networkService = new FXNetworkService(fxmlLoader.getController());
        networkService.doConnect();
        networkService.registerInterface(pcService);
        networkService.registerListeners();
        networkService.announce();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        networkService.unregisterInterface(pcService);
        networkService.unregisterListeners();
        networkService.doDisconnect();
    }

    public void createTrayIcon(final Stage stage) {
        if (SystemTray.isSupported()) {
            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            java.awt.Image image = null;
            try {
                URL iconUrl = getClass().getClassLoader().getResource("graphics/trayicon.png");
                image = ImageIO.read(iconUrl);
            } catch (IOException ex) {
                System.out.println(ex);
            }


            stage.setOnCloseRequest(t -> hide(stage));
            // create a action listener to listen for default action executed on the tray icon
            final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Platform.exit();
                    System.exit(0);
                }
            };

            ActionListener showListener = e -> Platform.runLater(stage::show);
            // create a popup menu
            PopupMenu popup = new PopupMenu();

            MenuItem closeItem = new MenuItem("Quit");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Title", popup);
            trayIcon.setImageAutoSize(true);
            // set the TrayIcon properties
            trayIcon.addActionListener(showListener);
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        }
    }

    public void showProgramIsMinimizedMsg() {
        if (firstTime) {
            trayIcon.displayMessage("IZConnect",
                    "App minimized to tray.",
                    TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }

    private void hide(final Stage stage) {
        Platform.runLater(() -> {
            if (SystemTray.isSupported()) {
                stage.hide();
                showProgramIsMinimizedMsg();
            } else {
                System.exit(0);
            }
        });
    }
}
