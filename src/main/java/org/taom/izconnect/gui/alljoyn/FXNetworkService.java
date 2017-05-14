package org.taom.izconnect.gui.alljoyn;

import org.alljoyn.bus.AboutDataListener;
import org.alljoyn.bus.AboutListener;
import org.alljoyn.bus.Observer;
import org.taom.izconnect.gui.alljoyn.listeners.AbstractListener;
import org.taom.izconnect.gui.alljoyn.listeners.BoardListener;
import org.taom.izconnect.gui.alljoyn.listeners.MobileListener;
import org.taom.izconnect.gui.alljoyn.listeners.PCListener;
import org.taom.izconnect.gui.controllers.Controller;
import org.taom.izconnect.network.AbstractNetworkService;

public class FXNetworkService extends AbstractNetworkService {
    private BoardListener boardListener;
    private MobileListener mobileListener;
    private PCListener pcListener;

    public FXNetworkService(Controller controller) {
        super();
        boardListener = new BoardListener(controller);
        mobileListener = new MobileListener(controller);
        pcListener = new PCListener(controller);
    }

    @Override
    protected AboutDataListener getAboutData() {
        return new DesktopAppAboutData();
    }

    @Override
    protected AboutListener getAboutListener() {
        return null;
    }

    @Override
    protected Observer.Listener getMobileListener() {
        return mobileListener;
    }

    @Override
    protected Observer.Listener getPCListener() {
        return pcListener;
    }

    @Override
    protected Observer.Listener getBoardListener() {
        return boardListener;
    }

    @Override
    protected String[] getInterestingInterfaces() {
        return new String[]{PACKAGE_NAME + ".interfaces."};
    }
}
