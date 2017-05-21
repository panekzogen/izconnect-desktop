package org.taom.izconnect.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.ProxyBusObject;
import org.taom.izconnect.gui.components.DevicesListItem;
import org.taom.izconnect.gui.components.ListViewCell;
import org.taom.izconnect.gui.utils.DeviceControlsFactory;
import org.taom.izconnect.gui.utils.DeviceItemFactory;
import org.taom.izconnect.network.interfaces.MobileInterface;

import java.io.IOException;

public class Controller {
    private static final String TAG = "Controller";

    @FXML
    private BorderPane mainPane;

    @FXML
    private ListView<DevicesListItem> devicesList;

    @FXML
    private Pane controlsPane;

    @FXML
    public Label statusBar;

    @FXML
    public ProgressBar progressBar;

    @FXML
    public void initialize() {
        devicesList.setCellFactory(param -> new ListViewCell());
        devicesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            DeviceControlsFactory.setViewForDevice(this, controlsPane.getChildren(), newValue);
        });
    }

    public void addDevice(DevicesListItem.DeviceType deviceType, ProxyBusObject proxyBusObject) {
        Platform.runLater(() -> {
            DevicesListItem devicesListItem = DeviceItemFactory.createDevice(deviceType, proxyBusObject);
            devicesList.getItems().add(devicesListItem);
        });
    }

    public void removeDevice(ProxyBusObject proxyBusObject) {
        Platform.runLater(() -> {
            DevicesListItem devicesListItem = DeviceItemFactory.removeDevice(proxyBusObject);
            if (devicesListItem != null) {
                devicesList.getItems().remove(devicesListItem);
            }
        });
    }
}
