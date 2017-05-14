package org.taom.izconnect.gui.alljoyn;

import org.taom.izconnect.network.AbstractAboutData;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DesktopAppAboutData extends AbstractAboutData {

    @Override
    protected String getDeviceName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return  "PC";
        }
    }

    @Override
    protected String getSoftwareVersion() {
        return System.getProperty("os.name");
    }
}
