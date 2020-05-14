package domain.studyGroupRepository.concreteSet;

import domain.studyGroup.StudyGroup;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 *  * Implementation ConcreteSet to get the smallest Study Group.
 */
public final class MinSet extends ConcreteSet {
    @Override
    public Set<StudyGroup> execute(Set<StudyGroup> studyGroups) {
        Comparator<StudyGroup> studyGroupComparator = new StudyGroup.StudyGroupComparator();

        StudyGroup minStudyGroup = findMin(studyGroupComparator, studyGroups);

        return new TreeSet<StudyGroup>(studyGroupComparator) {
            {
                add(minStudyGroup.clone());
            }
        };
    }

    private StudyGroup findMin(Comparator<StudyGroup> studyGroupComparator, Set<StudyGroup> studyGroups){
        StudyGroup minStudyGroup = null;

        for (StudyGroup studyGroup : studyGroups){
            if (minStudyGroup == null){
                minStudyGroup = studyGroup;
            }

            if (studyGroupComparator.compare(studyGroup, minStudyGroup)<0) {
                minStudyGroup = studyGroup;
            }
        }
        return minStudyGroup;
    }

}
