package org.taom.izconnect.gui.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import org.alljoyn.bus.ProxyBusObject;
import org.taom.izconnect.gui.components.DevicesListItem;
import org.taom.izconnect.gui.components.ListViewCell;
import org.taom.izconnect.gui.utils.DeviceItemFactory;

public class Controller {
    @FXML
    private BorderPane mainPane;

    @FXML
    private ListView<DevicesListItem> devicesList;

    @FXML
    public void initialize() {
        devicesList.setCellFactory(param -> new ListViewCell());
        devicesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            
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
