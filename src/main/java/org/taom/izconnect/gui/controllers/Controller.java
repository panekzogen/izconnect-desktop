package org.taom.izconnect.gui.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.alljoyn.bus.ProxyBusObject;

public class Controller {
    @FXML
    private VBox devicesBox;

    @FXML
    public void initialize() {
    }

    public void addMessage (String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label label = new Label("", );
                devicesBox.getChildren().add();
                ObservableList<String> mess = messages.getItems();
                mess.add(message);
                messages.setItems(mess);
            }
        });
    }

    public void addDevice(String name, ProxyBusObject device) {
        devicesMap.put(name, device);
    }
}
