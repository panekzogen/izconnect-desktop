package org.taom.izconnect.gui.utils;

import org.alljoyn.bus.ProxyBusObject;
import org.taom.izconnect.gui.components.DevicesListItem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeviceItemFactory {
    private static Map<DevicesListItem, ProxyBusObject> map = new ConcurrentHashMap<>();

    public static DevicesListItem createDevice(DevicesListItem.DeviceType deviceType, ProxyBusObject proxyBusObject) {
        String busName = proxyBusObject.getBusName();
        String deviceName = "device1";
        String deviceOS = "deviceOS";

        DevicesListItem devicesListItem = new DevicesListItem(busName, deviceType, deviceName, deviceOS);
        map.put(devicesListItem, proxyBusObject);
        return devicesListItem;
    }

    public static DevicesListItem removeDevice(ProxyBusObject proxyBusObject) {
        for (Map.Entry<DevicesListItem, ProxyBusObject> entry : map.entrySet()) {
            if (proxyBusObject == entry.getValue()) {
                map.remove(entry.getKey());
                return entry.getKey();
            }
        }
        return null;
    }

    public static ProxyBusObject getProxyBusObject(DevicesListItem devicesListItem) {
        return map.get(devicesListItem);
    }
}
