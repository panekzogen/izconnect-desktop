package org.taom.izconnect.gui.alljoyn;

import org.alljoyn.bus.Observer;
import org.alljoyn.bus.ProxyBusObject;
import org.taom.izconnect.gui.controllers.Controller;

public class FXObserverListener implements Observer.Listener {
    Controller controller;

    public FXObserverListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void objectDiscovered(ProxyBusObject obj) {
        System.out.println("founded");
    }

    @Override
    public void objectLost(ProxyBusObject proxyBusObject) {
        controller.removeDevice(proxyBusObject.getBusName());
    }
}
