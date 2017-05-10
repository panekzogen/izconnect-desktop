package org.taom.izconnect.gui.alljoyn;

import org.alljoyn.bus.AboutDataListener;
import org.alljoyn.bus.AboutListener;
import org.alljoyn.bus.Observer;
import org.taom.izconnect.gui.controllers.Controller;
import org.taom.izconnect.network.AbstractNetworkService;

public class FXNetworkService extends AbstractNetworkService {
    private FXAboutListener fxAboutListener;
    private FXObserverListener fxObserverListener;

    public FXNetworkService(Controller controller) {
        super();
        fxAboutListener = new FXAboutListener(controller);
        fxObserverListener = new FXObserverListener(controller);
    }

    @Override
    protected AboutDataListener getAboutData() {
        return new DesktopAppAboutData();
    }

    @Override
    protected AboutListener getAboutListener() {
        return fxAboutListener;
    }

    @Override
    protected Observer.Listener getObserverListener() {
        return fxObserverListener;
    }
}
