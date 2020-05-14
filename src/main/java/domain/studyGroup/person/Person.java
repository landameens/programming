package domain.studyGroup.person;

import domain.exception.VerifyException;
import domain.studyGroup.FormOfEducation;

public class Person implements Cloneable{
    private static final String EMPTY_VALUE = "Поле не должно быть пустым.";
    private static final String SHOULD_BE_POSITIVE = "Значение должно быть положительным.";
    private static final String NotUnic = "Такое значение уже соответствует другому человеку.";

    private String name; //Поле не может быть null, Строка не может быть пустой
    private int height; //Значение поля должно быть больше 0
    private String passportID; //Строка не может быть пустой, Значение этого поля должно быть уникальным, Поле не может быть null
    private Country nationality; //Поле может быть null

    public Person(String name,
                  int height,
                  String passportID,
                  Country nationality) throws VerifyException{
        checkName(name);
        this.name = name;
        checkHeight(height);
        this.height = height;
        checkPassportID(passportID);
        this.passportID = passportID;
        this.nationality = nationality;
    }

    private void checkName(String name) throws VerifyException{
        if (name == null){
            throw new VerifyException(EMPTY_VALUE);
        }

        if (name.equals("")){
            throw new VerifyException(EMPTY_VALUE);
        }
    }
    private void checkHeight(int height) throws VerifyException{
        if (height <= 0){
            throw new VerifyException(SHOULD_BE_POSITIVE);
        }
    }
    private void checkPassportID(String passportID) throws VerifyException{
        if (passportID == null){
            throw new VerifyException(EMPTY_VALUE);
        }

        if (passportID.equals("")){
            throw new VerifyException(EMPTY_VALUE);
        }
    }

    public static Person createPerson(PersonDTO groupAdminDTO) throws VerifyException {
        if (groupAdminDTO == null) {
            return null;
        }

        String name = groupAdminDTO.name;
        int height = groupAdminDTO.height;
        String passportID = groupAdminDTO.passportID;
        Country nationality;
        if (groupAdminDTO.nationality != null) {
            nationality = Country.getCountry(groupAdminDTO.nationality.toLowerCase());
        } else nationality = null;

        return new Person(name,
                            height,
                            passportID,
                            nationality);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public static Person getPerson(PersonDTO personDTO) throws VerifyException {
        return new Person( personDTO.name,
                personDTO.height,
                personDTO.passportID,
                Country.getCountry(personDTO.nationality.toLowerCase()));
    }

    public static PersonDTO getPersonDTO(Person person){
        PersonDTO personDTO = new PersonDTO();

        personDTO.name = person.getName();
        personDTO.height = person.getHeight();
        personDTO.passportID = person.getPassportID();
        personDTO.nationality = person.getNationality().getName();

        return personDTO;
    }

    @Override
    public Person clone() {
        try {
            return new Person(this.name,
                    this.height,
                    this.passportID,
                    this.nationality);
        } catch (VerifyException e) {
           throw new RuntimeException();
        }
    }


    @Override
    public String toString() {
        return  System.lineSeparator() +
                "  name= " + name + System.lineSeparator() +
                "  height= " + height + System.lineSeparator() +
                "  passportID= " + passportID + System.lineSeparator() +
                "  nationality= " + nationality;
    }

}
