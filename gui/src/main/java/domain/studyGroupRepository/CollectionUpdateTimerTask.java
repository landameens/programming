package domain.studyGroupRepository;

import controller.serverAdapter.exception.ServerAdapterException;
import domain.studyGroup.dao.ServerStudyGroupDAO;
import manager.LogManager;

import java.util.TimerTask;

public class CollectionUpdateTimerTask extends TimerTask {
    private final ServerStudyGroupDAO serverStudyGroupDAO;
    private final StudyGroupCollectionUpdater studyGroupCollectionUpdater;

    private final LogManager loggerAdapter = LogManager.createDefault(CollectionUpdateTimerTask.class);


    CollectionUpdateTimerTask(ServerStudyGroupDAO serverStudyGroupDAO,
                              StudyGroupCollectionUpdater studyGroupCollectionUpdater) {
        this.serverStudyGroupDAO = serverStudyGroupDAO;
        this.studyGroupCollectionUpdater = studyGroupCollectionUpdater;
    }


    @Override
    public void run() {
        try {
            studyGroupCollectionUpdater.update(serverStudyGroupDAO.get());
        } catch (ServerAdapterException e) {
            loggerAdapter.errorThrowable("The internal problem in work of Connection: ", e);
            studyGroupCollectionUpdater.disconnect();
        }
    }
}
