package org.taom.izconnect.gui.utils;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.ProxyBusObject;
import org.alljoyn.bus.Variant;
import org.taom.izconnect.gui.components.DevicesListItem;
import org.taom.izconnect.network.GFLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class DeviceItemFactory {
    private static Map<DevicesListItem, ProxyBusObject> map = new ConcurrentHashMap<>();

    public static DevicesListItem createDevice(DevicesListItem.DeviceType deviceType, ProxyBusObject proxyBusObject) {
        String busName = proxyBusObject.getBusName();

        String deviceName;
        String deviceOS;
        try {
            Map<String, Variant> map = proxyBusObject.getAllProperties(deviceType.getInterfaceClass());
            deviceName = map.get("DeviceName").getObject(String.class);
            deviceOS = map.get("DeviceOS").getObject(String.class);
        } catch (BusException e) {
            GFLogger.log(Level.SEVERE, "DeviceFactory", "Cannot get device info.");
            deviceName = "UNKNOWN";
            deviceOS = "UNKNOWN";
        }

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
