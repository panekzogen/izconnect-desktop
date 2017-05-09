package org.taom.izconnect.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.alljoyn.bus.Variant;
import org.taom.izconnect.gui.utils.DeviceItemFactory;

import java.util.Map;

public class Controller {
    @FXML
    private VBox devicesBox;

    @FXML
    public void initialize() {
    }

    public void addDevice(Map<String, Variant> map) {
        Platform.runLater(() -> {

            devicesBox.getChildren().add(DeviceItemFactory.createDeviceItem(map));
        });
    }
}
