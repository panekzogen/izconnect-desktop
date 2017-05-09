package org.taom.izconnect.gui.alljoyn;

import org.alljoyn.bus.Observer;
import org.alljoyn.bus.ProxyBusObject;

public class FXObserverListener implements Observer.Listener {
    @Override
    public void objectDiscovered(ProxyBusObject obj) {
        System.out.println("founded");
    }

    @Override
    public void objectLost(ProxyBusObject proxyBusObject) {

    }
}
