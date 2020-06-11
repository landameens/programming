package domain.studyGroupRepository;

import controller.serverAdapter.exception.ServerAdapterException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.dao.ServerStudyGroupDAO;
import manager.LogManager;

import java.util.Collections;
import java.util.List;

public class StudyGroupCollectionUpdater {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(StudyGroupCollectionUpdater.class);


    private static final int UPDATE_PERIOD = 60000;

    private List<StudyGroupRepositorySubscriber> subscribers;

    private Thread thread;
    private final ServerStudyGroupDAO serverStudyGroupDAO;


    public StudyGroupCollectionUpdater(ServerStudyGroupDAO serverStudyGroupDAO,
                                       List<StudyGroupRepositorySubscriber> subscribers) {
        this.subscribers = subscribers;
        this.serverStudyGroupDAO = serverStudyGroupDAO;
    }

    void update(List<StudyGroup> products)  {
        List<StudyGroup> list = Collections.unmodifiableList(products);
        subscribers.forEach(studyGroupRepositorySubscriber -> studyGroupRepositorySubscriber.change(list));
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
        }

        thread = null;
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(() -> {
                while (true) {
                    try {
                        update(serverStudyGroupDAO.get());
                    } catch (ServerAdapterException e) {
                        LOG_MANAGER.errorThrowable("The internal problem in work of Connection: ", e);
                        disconnect();
                    }

                    try {
                        Thread.sleep(UPDATE_PERIOD);
                    } catch (InterruptedException e) {
                        LOG_MANAGER.errorThrowable(e);
                        break;
                    }
                }
            });
            thread.start();
        }
    }

    void disconnect() {
        subscribers.forEach(StudyGroupRepositorySubscriber::disconnect);
    }
}
