package org.taom.izconnect.gui.components;

import javafx.scene.control.ListCell;

public class ListViewCell extends ListCell<DevicesListItem> {
    @Override
    protected void updateItem(DevicesListItem item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            DeviceItemHolder deviceItemHolder = new DeviceItemHolder(item);
            setGraphic(deviceItemHolder.getBox());
        } else {
            setGraphic(null);
        }
    }
}
