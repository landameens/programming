package domain.studyGroupFactory;

import domain.exception.CreationException;
import domain.exception.VerifyException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;

/**
 * Interface for StudyGroup Factory.
 */
public interface IStudyGroupFactory {

    StudyGroup createNewStudyGroup(StudyGroupDTO studyGroupDTO) throws VerifyException, CreationException;
}