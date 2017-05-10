package org.taom.izconnect.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.Variant;
import org.taom.izconnect.gui.utils.DeviceItemFactory;
import org.taom.izconnect.network.interfaces.SampleInterface;
import org.taom.izconnect.network.interfaces.SampleService;

import java.util.Map;

public class Controller {
    @FXML
    private BorderPane mainPane;

    @FXML
    private VBox devicesBox;

    @FXML
    public void initialize() {
    }

    public void addDevice(String busName, Map<String, Variant> map) {
        Platform.runLater(() -> devicesBox.getChildren().add(DeviceItemFactory.createDeviceItem(busName, map)));
    }

    public void removeDevice(String busName) {
        Platform.runLater(() -> devicesBox.getChildren().remove(DeviceItemFactory.removeDeviceItem(busName)));
    }
}
