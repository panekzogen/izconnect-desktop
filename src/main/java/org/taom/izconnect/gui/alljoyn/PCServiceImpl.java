package org.taom.izconnect.gui.alljoyn;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.BusObject;
import org.taom.izconnect.network.GFLogger;
import org.taom.izconnect.network.interfaces.PCInterface;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class PCServiceImpl implements BusObject, PCInterface {
    private static final String NIRCMD_PATH = "tools/nircmd/nircmd.exe";
    private static final String TAG = "PCInterface";

    private TrayIcon trayIcon;

    private int volumeLevel = 0;
    private Robot robot;

    private static int VK_MEDIA_PLAY_PAUSE = 0xB3;
    private static int VK_MEDIA_STOP = 0xB2;
    private static int VK_MEDIA_NEXT_TRACK = 0xB0;
    private static int VK_MEDIA_PREV_TRACK = 0xB1;

    public PCServiceImpl(TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot create robot");
        }
    }

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
    public void setVolume(int level) throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " setsysvolume " + level);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot change system volume");
        }
        volumeLevel = level;
    }

    @Override
    public int getVolume() throws BusException {
        return volumeLevel;
    }

    @Override
    public void mediaControlPlayPause() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendkeypress " + VK_MEDIA_PLAY_PAUSE);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit media control");
        }
    }

    @Override
    public void mediaControlStop() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendkeypress " + VK_MEDIA_STOP);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit media control");
        }
    }

    @Override
    public void mediaControlNext() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendkeypress " + VK_MEDIA_NEXT_TRACK);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit media control");
        }
    }

    @Override
    public void mediaControlPrevious() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendkeypress " + VK_MEDIA_PREV_TRACK);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit media control");
        }
    }

    @Override
    public void mouseMove(int x, int y) throws BusException {
        Point point = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(point.x + x, point.y + y);
    }

    @Override
    public void mouseLeftClick() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendmouse left click ");
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit mouse click");
        }
    }

    @Override
    public void mouseRightClick() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendmouse right click ");
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit mouse click");
        }
    }

    @Override
    public void keyPressed(int code) throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendkeypress " + code);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit key press");
        }
    }

    @Override
    public void slideshowStart() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendkeypress " + KeyEvent.VK_F5);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit key press");
        }
    }

    @Override
    public void slideshowStop() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendkeypress " + KeyEvent.VK_ESCAPE);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit key press");
        }
    }

    @Override
    public void nextSlide() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendkeypress " + KeyEvent.VK_RIGHT);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit key press");
        }
    }

    @Override
    public void previousSlide() throws BusException {
        try {
            Runtime.getRuntime().exec(PCServiceImpl.NIRCMD_PATH + " sendkeypress " + KeyEvent.VK_LEFT);
        } catch (IOException e) {
            GFLogger.log(Level.SEVERE, TAG, "Cannot emit key press");
        }
    }

    @Override
    public void subscribe(String busName) throws BusException {

    }

    @Override
    public void notify(String deviceName, String sender, String notification) throws BusException {
        trayIcon.displayMessage(sender + " (" + deviceName + ")",
                notification,
                TrayIcon.MessageType.INFO);
    }

    @Override
    public void unsubscribe(String busName) throws BusException {

    }
}
