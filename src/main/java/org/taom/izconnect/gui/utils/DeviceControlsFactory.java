package org.taom.izconnect.gui.utils;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.taom.izconnect.gui.controllers.Controller;
import org.taom.izconnect.network.GFLogger;

import java.io.IOException;
import java.util.logging.Level;

import static org.taom.izconnect.gui.components.DevicesListItem.DeviceType;

public class DeviceControlsFactory {
    public static void setViewForDevice(Controller controller, ObservableList<Node> children, DeviceType deviceType) {
        switch(deviceType) {
            case BOARD:
                setView(controller, children, "boardPane.fxml");
                break;
            case MOBILE:
                setView(controller, children, "mobilePane.fxml");
                break;
            case PC:
                setView(controller, children, "pcPane.fxml");
                break;
            case UNKNOWN:
            default:
                break;
        }
    }

    private static void setView(Controller controller, ObservableList<Node> children, String fxmlPath) {
        children.clear();
        try {
            children.add(FXMLLoader.load(controller.getClass().getClassLoader().getResource(fxmlPath)));
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, "DeviceControlsFactory", "Can't set controls view");
        }
    }
}
