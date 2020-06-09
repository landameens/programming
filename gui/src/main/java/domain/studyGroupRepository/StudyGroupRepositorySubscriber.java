package domain.studyGroupRepository;

import domain.studyGroup.StudyGroup;

import java.util.List;

public interface StudyGroupRepositorySubscriber {
    /**
     * @param products unmodifiableList, new instance each time
     */
    void change(List<StudyGroup> products);
    void disconnect();
}
