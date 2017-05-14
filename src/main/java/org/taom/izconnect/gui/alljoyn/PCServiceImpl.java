package org.taom.izconnect.gui.alljoyn;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;
import org.taom.izconnect.network.interfaces.PCInterface;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PCServiceImpl implements BusObject, PCInterface {
    @Override
    public String getDeviceName() throws BusException {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return  "PC";
        }
    }

    @Override
    public String getDeviceOS() throws BusException {
        return System.getProperty("os.name");
    }

    @Override
    public void message(String str) throws BusException {

    }
}
