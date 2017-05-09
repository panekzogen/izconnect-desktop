package org.taom.izconnect.gui.alljoyn;

import org.taom.izconnect.gui.controllers.Controller;
import org.taom.izconnect.network.AbstractNetworkService;

public class FXNetworkService extends AbstractNetworkService {
    public FXNetworkService(Controller controller) {
        super(new FXAboutListener(controller), new FXObserverListener());
    }
}
