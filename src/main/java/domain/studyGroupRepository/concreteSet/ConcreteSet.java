package domain.studyGroupRepository.concreteSet;

import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;

import java.util.Set;

/**
 * Abstraction for getting different sets of Study Groups.
 */
public abstract class ConcreteSet {
    public abstract Set<StudyGroup> execute(Set<StudyGroup> studyGroups) throws StudyGroupRepositoryException;
}
