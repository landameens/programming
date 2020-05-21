package domain.studyGroupRepository.concreteSet;

import domain.studyGroup.StudyGroup;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Implementation of ConcreteSet to get a set of all groups.
 */
public final class AllSet extends ConcreteSet {

    @Override
    public Set<StudyGroup> execute(Set<StudyGroup> studyGroups) {
        Comparator<StudyGroup> studyGroupComparator = new StudyGroup.StudyGroupComparator();
        Set<StudyGroup> finalStudyGroup = new TreeSet<StudyGroup>(studyGroupComparator);

//        for(StudyGroup studyGroup : studyGroups) {
//            finalStudyGroup.add(studyGroup.clone());
//        }

        finalStudyGroup = studyGroups.stream().map(StudyGroup::clone).collect(Collectors.toSet());

        return finalStudyGroup;
    }
}
