package org.taom.izconnect.gui.alljoyn.listeners;

import org.alljoyn.bus.Observer;
import org.alljoyn.bus.ProxyBusObject;
import org.taom.izconnect.gui.components.DevicesListItem;
import org.taom.izconnect.gui.controllers.Controller;

public abstract class AbstractListener implements Observer.Listener {
    private Controller controller;

    public AbstractListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void objectDiscovered(ProxyBusObject proxyBusObject) {
        controller.addDevice(getDeviceType(), proxyBusObject);
    }

    @Override
    public void objectLost(ProxyBusObject proxyBusObject) {
        controller.removeDevice(proxyBusObject);
    }

    protected abstract DevicesListItem.DeviceType getDeviceType();
}
