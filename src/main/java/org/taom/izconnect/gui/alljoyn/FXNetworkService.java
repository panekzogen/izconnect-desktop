package org.taom.izconnect.gui.alljoyn;

import org.alljoyn.bus.AboutDataListener;
import org.taom.izconnect.gui.controllers.Controller;
import org.taom.izconnect.network.AbstractNetworkService;

public class FXNetworkService extends AbstractNetworkService {
    public FXNetworkService(Controller controller) {
        super(new FXAboutListener(controller), new FXObserverListener());
    }

    @Override
    protected AboutDataListener getAboutData() {
        return new DesktopAppAboutData();
    }
}
