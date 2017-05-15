package org.taom.izconnect.gui.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.alljoyn.bus.BusException;
import org.alljoyn.bus.ProxyBusObject;
import org.taom.izconnect.gui.components.DevicesListItem;
import org.taom.izconnect.gui.components.SwitchButton;
import org.taom.izconnect.gui.controllers.Controller;
import org.taom.izconnect.network.GFLogger;
import org.taom.izconnect.network.interfaces.BoardInterface;
import org.taom.izconnect.network.interfaces.MobileInterface;

import java.io.IOException;
import java.util.logging.Level;

import static org.taom.izconnect.gui.components.DevicesListItem.DeviceType;

public class DeviceControlsFactory {
    private static String busName;

    public static void setViewForDevice(Controller controller, ObservableList<Node> children, DevicesListItem device) {
        DeviceType deviceType = device.getDeviceType();
        VBox child;
        switch (deviceType) {
            case BOARD:
                child = (VBox) setView(controller, children, "boardPane.fxml");
                SwitchButton lightControl = (SwitchButton) child.lookup("#lightControl");
                lightControl.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        BoardInterface boardInterface = DeviceItemFactory.getProxyBusObject(device).getInterface(BoardInterface.class);
                        try {
                            boardInterface.setLight(newValue);
                        } catch (BusException e) {
                            e.printStackTrace();
                        }
                    }
                });

                break;
            case MOBILE:
                child = (VBox)setView(controller, children, "mobilePane.fxml");
                SwitchButton notificationsControl = (SwitchButton) child.lookup("#notificationsControl");
                notificationsControl.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        MobileInterface mobileInterface = DeviceItemFactory.getProxyBusObject(device).getInterface(MobileInterface.class);
                        try {
                            if (newValue) {
                                mobileInterface.subscribe(busName);
                            } else {
                                mobileInterface.unsubscribe(busName);
                            }
                        } catch (BusException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case PC:
                setView(controller, children, "pcPane.fxml");
                break;
            case UNKNOWN:
            default:
                break;
        }
    }

    private static Node setView(Controller controller, ObservableList<Node> children, String fxmlPath) {
        children.clear();
        Node child = null;
        try {
            child = FXMLLoader.load(controller.getClass().getClassLoader().getResource(fxmlPath));
            children.add(child);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, "DeviceControlsFactory", "Can't set controls view");
        }
        return child;
    }

    public static void setBusName(String busName) {
        DeviceControlsFactory.busName = busName;
    }
}
