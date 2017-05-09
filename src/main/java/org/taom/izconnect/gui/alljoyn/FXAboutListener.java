package org.taom.izconnect.gui.alljoyn;

import org.alljoyn.bus.AboutListener;
import org.alljoyn.bus.AboutObjectDescription;
import org.alljoyn.bus.Variant;
import org.taom.izconnect.gui.controllers.Controller;

import java.util.Map;

public class FXAboutListener implements AboutListener {
    Controller controller;

    public FXAboutListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void announced(String s, int i, short i1, AboutObjectDescription[] aboutObjectDescriptions, Map<String, Variant> map) {
        controller.addDevice(map);
    }
}
