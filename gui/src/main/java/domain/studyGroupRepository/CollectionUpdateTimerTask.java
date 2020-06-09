package domain.studyGroupRepository;

import controller.serverAdapter.exception.ServerAdapterException;
import domain.studyGroup.dao.ServerStudyGroupDAO;
import manager.LogManager;

import java.util.TimerTask;

public class CollectionUpdateTimerTask extends TimerTask {
    private final ServerStudyGroupDAO serverStudyGroupDAO;
    private final ProductCollectionUpdater productCollectionUpdater;

    private final LogManager loggerAdapter = LogManager.createDefault(CollectionUpdateTimerTask.class);


    CollectionUpdateTimerTask(ServerStudyGroupDAO serverStudyGroupDAO,
                              ProductCollectionUpdater productCollectionUpdater) {
        this.serverStudyGroupDAO = serverStudyGroupDAO;
        this.productCollectionUpdater = productCollectionUpdater;
    }


    @Override
    public void run() {
        try {
            productCollectionUpdater.update(serverStudyGroupDAO.get());
        } catch (ServerAdapterException e) {
            loggerAdapter.errorThrowable("The internal problem in work of Connection: ", e);
            productCollectionUpdater.disconnect();
        }
    }
}
