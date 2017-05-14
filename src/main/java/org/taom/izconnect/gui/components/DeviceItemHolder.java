package org.taom.izconnect.gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class DeviceItemHolder {
    @FXML
    private HBox hBox;

    @FXML
    private Label deviceName;

    @FXML
    private ImageView deviceIcon;

    public DeviceItemHolder(DevicesListItem devicesListItem) {
        deviceName = new Label(devicesListItem.getDeviceName() + "\n" + devicesListItem.getDeviceOS());
        deviceName.setMinSize(130, 40);
        deviceName.setMaxSize(140, 40);
//        deviceName.getStylesheets().add("styles/menu.css");
//        deviceName.getStyleClass().add("device");

        deviceIcon = new ImageView(devicesListItem.getDeviceType().getIconPath());
        deviceIcon.setFitHeight(40);
        deviceIcon.setFitWidth(40);

        hBox = new HBox(deviceName, deviceIcon);
    }

    public HBox getBox() {
        return hBox;
    }
}
