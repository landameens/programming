package domain.studyGroupFactory;

import domain.exception.VerifyException;
import domain.studyGroup.FormOfEducation;
import domain.studyGroup.Semester;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;
import domain.studyGroup.coordinates.Coordinates;
import domain.studyGroup.person.Person;
import domain.studyGroupFactory.idProducer.IdProducer;

import java.time.LocalDateTime;

/**
 * Class for create new study group with study group DTO.
 */
public class StudyGroupFactory implements IStudyGroupFactory {

    private final IdProducer idProducer;

    public StudyGroupFactory(IdProducer idProducer){
        this.idProducer = idProducer;
    }

    public IdProducer getIdProducer() {
        return idProducer;
    }

    /**
     * This method creates a new instance of the study group when it receives the studu group DTO.
     * @param studyGroupDTO
     * @return new studyGroup
     * @throws VerifyException
     */
    @Override
    public StudyGroup createNewStudyGroup(StudyGroupDTO studyGroupDTO) throws VerifyException {
        if (studyGroupDTO == null){
            return null;
        }

        Long id = idProducer.getId();
        String name = studyGroupDTO.name;
        Coordinates coordinates = Coordinates.createCoordinates(studyGroupDTO.coordinates);
        LocalDateTime creationDate = LocalDateTime.now();
        int studentsCount = studyGroupDTO.studentsCount;
        Long shouldBeExpelled = studyGroupDTO.shouldBeExpelled;
        FormOfEducation formOfEducation;
        if (studyGroupDTO.formOfEducation != null) {
            formOfEducation = FormOfEducation.getFormOfEducation(studyGroupDTO.formOfEducation.toLowerCase());
        } else formOfEducation = null;
        Semester semesterEnum = Semester.getSemesterEnum(studyGroupDTO.semesterEnum.toLowerCase());
        Person groupAdmin = Person.createPerson(studyGroupDTO.groupAdmin);



        return new StudyGroup(id,
                            name,
                            coordinates,
                            creationDate,
                            studentsCount,
                            shouldBeExpelled,
                            formOfEducation,
                            semesterEnum,
                            groupAdmin);
    }
}
