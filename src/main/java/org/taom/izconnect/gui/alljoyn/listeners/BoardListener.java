package org.taom.izconnect.gui.alljoyn.listeners;

import org.taom.izconnect.gui.components.DevicesListItem;
import org.taom.izconnect.gui.controllers.Controller;

import static org.taom.izconnect.gui.components.DevicesListItem.*;

public class BoardListener extends AbstractListener {
    public static final DeviceType DEVICE_TYPE = DeviceType.BOARD;

    public BoardListener(Controller controller) {
        super(controller);
    }

    @Override
    protected DeviceType getDeviceType() {
        return DEVICE_TYPE;
    }
}
