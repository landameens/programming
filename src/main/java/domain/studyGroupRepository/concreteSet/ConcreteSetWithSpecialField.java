package domain.studyGroupRepository.concreteSet;

import domain.exception.StudyGroupRepositoryException;
import domain.studyGroup.StudyGroup;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation ConcreteSet to get a set of Study Groups which have equals field.
 */
public final class ConcreteSetWithSpecialField extends ConcreteSet {

    private final String field;
    private final Object value;
    private final Class<?> clazz;

    public ConcreteSetWithSpecialField(Class<?> clazz,
                                       String field,
                                       Object value){
        this.field = field;
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public Set<StudyGroup> execute(Set<StudyGroup> studyGroups) throws StudyGroupRepositoryException {
        Comparator<StudyGroup> studyGroupComparator = new StudyGroup.StudyGroupComparator();
        Set<StudyGroup> finalStudyGroup = new TreeSet<>(studyGroupComparator);

        Field clazzField;
        try {
            clazzField = clazz.getDeclaredField(field);
            clazzField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new StudyGroupRepositoryException(e);
        }

        for (StudyGroup studyGroup : studyGroups){
            try {
                if(clazzField.get(studyGroup).equals(value)) {
                    finalStudyGroup.add(studyGroup.clone());
                }
            } catch (IllegalAccessException e) {
                throw new StudyGroupRepositoryException(e);
            }
        }

        clazzField.setAccessible(false);

        return finalStudyGroup;
    }
}
