package domain.studyGroupRepository;

import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import storage.studyGroupDAO.Saveable;

import java.util.Set;

/**
 * Interface for working with the repository where Study Groups stored.
 * To get a set of Study Groups using the abstract class ConcreteSet.
 */
public interface IStudyGroupRepository extends Saveable {
    void add(StudyGroupDTO studyGroupDTO) throws StudyGroupRepositoryException;

    void remove(StudyGroup studyGroup) throws StudyGroupRepositoryException;

    void update(StudyGroup studyGroup) throws StudyGroupRepositoryException;

    Set<StudyGroup> getConcreteSetOfStudyGroups(ConcreteSet concreteSet) throws StudyGroupRepositoryException;

    CollectionInfo getInfo();

}
