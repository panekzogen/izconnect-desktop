package org.taom.izconnect.gui.alljoyn.listeners;

import org.taom.izconnect.gui.controllers.Controller;

import static org.taom.izconnect.gui.components.DevicesListItem.DeviceType;

public class MobileListener extends AbstractListener {
    public static final DeviceType DEVICE_TYPE = DeviceType.MOBILE;

    public MobileListener(Controller controller) {
        super(controller);
    }

    @Override
    protected DeviceType getDeviceType() {
        return DEVICE_TYPE;
    }
}
