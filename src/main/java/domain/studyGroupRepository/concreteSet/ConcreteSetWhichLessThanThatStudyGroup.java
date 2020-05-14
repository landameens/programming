package domain.studyGroupRepository.concreteSet;

import domain.studyGroup.StudyGroup;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation ConcreteSet to get a set of Study Groups which less than that Study Group.
 */
public final class ConcreteSetWhichLessThanThatStudyGroup extends ConcreteSet {

    private final StudyGroup studyGroupForComparing;

    public ConcreteSetWhichLessThanThatStudyGroup(StudyGroup studyGroupForComparing){

        this.studyGroupForComparing = studyGroupForComparing;
    }

    @Override
    public Set<StudyGroup> execute(Set<StudyGroup> studyGroups) {
        Comparator<StudyGroup> studyGroupComparator = new StudyGroup.StudyGroupComparator();
        Set<StudyGroup> finalStudyGroupSet = new TreeSet<>(studyGroupComparator);

        for(StudyGroup studyGroup : studyGroups) {
            if (studyGroupComparator.compare(studyGroup, studyGroupForComparing) < 0) {
                finalStudyGroupSet.add(studyGroup.clone());
            }
        }

        return finalStudyGroupSet;
    }

}
