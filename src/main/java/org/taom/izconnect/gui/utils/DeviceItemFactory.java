package org.taom.izconnect.gui.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.Variant;

import java.util.HashMap;
import java.util.Map;

public class DeviceItemFactory {
    private static Map <String, Label> busNameToLabel = new HashMap<>();

    public static Label createDeviceItem(String busName, Map<String, Variant> map) {
        String deviceName = "";
        try {
            deviceName = map.get("DeviceName").getObject(String.class);
        } catch (BusException e) {
            e.printStackTrace();
        }

        ImageView image = new ImageView(deviceName.contains("Android") ? "graphics/mobile.png" : "graphics/pc.png");
        image.setFitHeight(40);
        image.setFitWidth(40);

        String[] namePaths = deviceName.split(";");
        deviceName = "";
        for (String path : namePaths) {
            deviceName += path + "\n";
        }

        Label label = new Label(deviceName, image);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setTextAlignment(TextAlignment.RIGHT);
        label.setMaxSize(170, 40);
        label.getStylesheets().add("styles/menu.css");
        label.getStyleClass().add("device");

        busNameToLabel.put(busName, label);
        return label;
    }

    public static Label removeDeviceItem(String busName) {
        return busNameToLabel.remove(busName);
    }
}
