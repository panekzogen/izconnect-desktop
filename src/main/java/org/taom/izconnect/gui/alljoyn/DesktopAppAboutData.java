package org.taom.izconnect.gui.alljoyn;

import org.taom.izconnect.network.AbstractAboutData;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DesktopAppAboutData extends AbstractAboutData {

    @Override
    protected String getDeviceName() {
        StringBuilder deviceName = new StringBuilder();
        try {
            deviceName.append(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            deviceName.append("PC");
        } finally {
            deviceName.append(" (").append(System.getProperty("os.name")).append(")");
        }
        return deviceName.toString();
    }
}
