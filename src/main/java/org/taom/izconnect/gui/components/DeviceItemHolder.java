package org.taom.izconnect.gui.components;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class DeviceItemHolder {
    @FXML
    private HBox hBox;

    @FXML
    private Label deviceName;

    @FXML
    private ImageView deviceIcon;

    public DeviceItemHolder(DevicesListItem devicesListItem) {
        deviceName = new Label(devicesListItem.getDeviceName() + "\n" + devicesListItem.getDeviceOS());
        deviceName.setMinSize(170, 50);
        deviceName.setMaxSize(170, 50);
//        deviceName.getStylesheets().add("styles/menu.css");
//        deviceName.getStyleClass().add("device");

        deviceIcon = new ImageView(devicesListItem.getDeviceType().getIconPath());
        deviceIcon.setFitHeight(50);
        deviceIcon.setFitWidth(50);

        hBox = new HBox(deviceName, deviceIcon);
    }

    public HBox getBox() {
        return hBox;
    }
}
