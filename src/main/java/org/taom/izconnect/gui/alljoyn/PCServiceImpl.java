package org.taom.izconnect.gui.alljoyn;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;
import org.taom.izconnect.network.interfaces.PCInterface;

public class PCServiceImpl implements BusObject, PCInterface {
    @Override
    public void message(String str) throws BusException {

    }
}
