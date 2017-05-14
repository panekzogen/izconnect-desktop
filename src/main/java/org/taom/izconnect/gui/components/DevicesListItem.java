package org.taom.izconnect.gui.components;

import org.taom.izconnect.network.interfaces.BoardInterface;
import org.taom.izconnect.network.interfaces.MobileInterface;
import org.taom.izconnect.network.interfaces.PCInterface;

public class DevicesListItem {

    public enum DeviceType {
        BOARD(1, "graphics/board.png", BoardInterface.class),
        MOBILE(2, "graphics/mobile.png", MobileInterface.class),
        PC(3, "graphics/pc.png", PCInterface.class),
        UNKNOWN(4, "", null);

        private final int id;
        private final String iconPath;
        private final Class interfaceClass;

        DeviceType(int id, String iconPath, Class interfaceClass) {
            this.id = id;
            this.iconPath = iconPath;
            this.interfaceClass = interfaceClass;
        }

        public int getId() {
            return id;
        }

        public String getIconPath() {
            return iconPath;
        }

        public Class getInterfaceClass() {
            return interfaceClass;
        }

        public static DeviceType valueOf(int id) {
            switch (id) {
                case 1:
                    return BOARD;
                case 2:
                    return MOBILE;
                case 3:
                    return PC;
                default:
                    return UNKNOWN;
            }
        }
    }

    private String busName;
    private DeviceType deviceType;
    private String deviceName;
    private String deviceOS;

    public DevicesListItem(String busName, DeviceType deviceType, String deviceName, String deviceOS) {
        this.busName = busName;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.deviceOS = deviceOS;
    }

    public String getBusName() {
        return busName;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

}
