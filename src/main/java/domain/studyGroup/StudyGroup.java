package domain.studyGroup;

import domain.exception.VerifyException;
import domain.studyGroup.coordinates.Coordinates;
import domain.studyGroup.person.Person;

import java.time.LocalDateTime;
import java.util.Comparator;

public class StudyGroup implements Cloneable{

    private static final String SHOULD_BE_POSITIVE = "Значение должно быть положительным.";
    private static final String EMPTY_EXCEPTION = "Значение не должно быть пустым";

    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int studentsCount; //Значение поля должно быть больше 0
    private Long shouldBeExpelled; //Значение поля должно быть больше 0, Поле может быть null
    private FormOfEducation formOfEducation; //Поле может быть null
    private Semester semesterEnum; //Поле может быть null
    private Person groupAdmin; //Поле не может быть null

    public StudyGroup(Long id,
                      String name,
                      Coordinates coordinates,
                      LocalDateTime creationDate,
                      int studentsCount,
                      Long shouldBeExpelled,
                      FormOfEducation formOfEducation,
                      Semester semesterEnum,
                      Person groupAdmin) throws VerifyException{
        checkId(id);
        this.id = id;
        checkName(name);
        this.name = name;
        checkCoordinates(coordinates);
        this.coordinates = coordinates;
        checkCreationDate(creationDate);
        this.creationDate = creationDate;
        checkStudentsCount(studentsCount);
        this.studentsCount = studentsCount;
        checkShouldBeExpelled(shouldBeExpelled);
        this.shouldBeExpelled = shouldBeExpelled;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        //checkGroupAdmin(groupAdmin);
        this.groupAdmin = groupAdmin;
    }

    private void checkId(Long id) throws VerifyException {
        if (id == null){
            throw new VerifyException(EMPTY_EXCEPTION);
        }

        if (id <= 0){
            throw new VerifyException(SHOULD_BE_POSITIVE);
        }
    }

    private void checkName(String name) throws VerifyException{
        if (name == null){
            throw new VerifyException(EMPTY_EXCEPTION);
        }

        if (name.equals("")){
            throw new VerifyException(EMPTY_EXCEPTION);
        }
    }

    private void checkStudentsCount(int studentsCount) throws VerifyException{
        if (studentsCount <= 0){
            throw new VerifyException(SHOULD_BE_POSITIVE);
        }
    }

    private void checkShouldBeExpelled(Long shouldBeExpelled) throws VerifyException{
        if (shouldBeExpelled <= 0){
            throw new VerifyException(SHOULD_BE_POSITIVE);
        }
    }

    private void checkCoordinates(Coordinates coordinates) throws VerifyException {
        if (coordinates == null){
            throw new VerifyException(EMPTY_EXCEPTION);
        }
    }

    private void checkCreationDate(LocalDateTime creationDate) throws VerifyException {
        if (creationDate == null){
            throw new VerifyException(EMPTY_EXCEPTION);
        }
    }

    private void checkGroupAdmin(Person groupAdmin) throws VerifyException{
        if (groupAdmin == null || groupAdmin != null) {
            throw new VerifyException(EMPTY_EXCEPTION);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) throws VerifyException {
        checkId(id);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws VerifyException {
        checkName(name);
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {

        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(int studentsCount) {
        this.studentsCount = studentsCount;
    }

    public Long getShouldBeExpelled() {
        return shouldBeExpelled;
    }

    public void setShouldBeExpelled(Long shouldBeExpelled) {
        this.shouldBeExpelled = shouldBeExpelled;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public void setSemesterEnum(Semester semesterEnum) {
        this.semesterEnum = semesterEnum;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof StudyGroup)) {
            return false;
        }

        StudyGroup that = (StudyGroup) o;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return (int) Long.parseLong(id.toString());
    }

    @Override
    public String toString() {
        return "StudyGroup" + id + System.lineSeparator() +
                " id= " + id + System.lineSeparator() +
                " name= " + name + System.lineSeparator() +
                " coordinates (" + coordinates + ")" + System.lineSeparator() +
                " creationDate= " + creationDate + System.lineSeparator() +
                " studentsCount= " + studentsCount + System.lineSeparator() +
                " shouldBeExpelled= " + shouldBeExpelled + System.lineSeparator() +
                " formOfEducation= " + formOfEducation + System.lineSeparator() +
                " semesterEnum= " + semesterEnum + System.lineSeparator() +
                " groupAdmin " + groupAdmin;
    }

    public static class StudyGroupComparator implements Comparator<StudyGroup>{
        @Override
        public int compare(StudyGroup o1, StudyGroup o2) {
            return o1.studentsCount - o2.studentsCount;
        }
    }

    public static class StudyGroupDTOComparator implements Comparator<StudyGroupDTO>{
        @Override
        public int compare(StudyGroupDTO o1, StudyGroupDTO o2) {
            return o1.studentsCount - o2.studentsCount;
        }
    }

    @Override
    public StudyGroup clone() {
        try {
            return new StudyGroup(this.id,
                    this.name,
                    this.coordinates.clone(),
                    this.creationDate,
                    this.studentsCount,
                    this.shouldBeExpelled,
                    this.formOfEducation,
                    this.semesterEnum,
                    this.groupAdmin == null ? null : this.groupAdmin.clone());
        } catch (VerifyException e) {
            throw new RuntimeException();
        }
    }

    /**
     * this method returns the Study Group getting the Study Group DTO
     * @param studyGroupDTO
     * @return StudyGroup
     * @throws VerifyException
     */
    public static StudyGroup getStudyGroup(StudyGroupDTO studyGroupDTO) throws VerifyException {
        return new StudyGroup(studyGroupDTO.id,
                studyGroupDTO.name,
                Coordinates.getCoordinates(studyGroupDTO.coordinates),
                studyGroupDTO.creationDate,
                studyGroupDTO.studentsCount,
                studyGroupDTO.shouldBeExpelled,
                FormOfEducation.getFormOfEducation(studyGroupDTO.formOfEducation.toLowerCase()),
                Semester.getSemesterEnum(studyGroupDTO.semesterEnum.toLowerCase()),
                Person.getPerson(studyGroupDTO.groupAdmin));
    }

    /**
     * This method returns Study Group DTO by Study Group.
     * @param studyGroup
     * @return StudyGroupDTO
     */
    public static StudyGroupDTO getStudyGroupDTO(StudyGroup studyGroup) {
        StudyGroupDTO studyGroupDTO = new StudyGroupDTO();

        studyGroupDTO.id = studyGroup.getId();
        studyGroupDTO.name = studyGroup.getName();
        studyGroupDTO.coordinates = Coordinates.getCoordinatesDTO(studyGroup.getCoordinates());
        studyGroupDTO.creationDate = studyGroup.getCreationDate();
        studyGroupDTO.studentsCount = studyGroup.getStudentsCount();
        studyGroupDTO.shouldBeExpelled = studyGroup.getShouldBeExpelled();
        studyGroupDTO.formOfEducation = studyGroup.getFormOfEducation().getName();
        studyGroupDTO.semesterEnum = studyGroup.getSemesterEnum().getName();
        studyGroupDTO.groupAdmin = Person.getPersonDTO(studyGroup.getGroupAdmin());

        return studyGroupDTO;
    }
}
