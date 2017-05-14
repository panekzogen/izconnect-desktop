package org.taom.izconnect.gui.components;

public class DevicesListItem {

    public enum DeviceType {
        BOARD(1, ""),
        MOBILE(2, "graphics/mobile.png"),
        PC(3, "graphics/pc.png"),
        UNKNOWN(4, "");

        private final int id;
        private final String iconPath;

        DeviceType(int id, String iconPath) {
            this.id = id;
            this.iconPath = iconPath;
        }

        public int getId() {
            return id;
        }

        public String getIconPath() {
            return iconPath;
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
