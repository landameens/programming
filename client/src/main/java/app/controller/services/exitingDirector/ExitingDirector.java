package client.controller.services.exitingDirector;

import com.google.inject.Inject;
import controller.components.serviceMediator.Service;

import java.util.List;

public final class ExitingDirector implements Service {
    private final List<INeedExiting> needExitList;


    @Inject
    public ExitingDirector(List<INeedExiting> needExitList) {
        this.needExitList = needExitList;
    }


    public void exit() {
        needExitList.forEach(INeedExiting::exit);
    }

    public void addINeedExiting(INeedExiting iNeedExiting) {
        needExitList.add(iNeedExiting);
    }
}
