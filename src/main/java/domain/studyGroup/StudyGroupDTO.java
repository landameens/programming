package domain.studyGroup;

import domain.studyGroup.coordinates.CoordinatesDTO;
import domain.studyGroup.person.PersonDTO;
import storage.adapters.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlType(name = "StudyGroup")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class StudyGroupDTO {
    public Long id;
    public String name;
    public CoordinatesDTO coordinates;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public java.time.LocalDateTime creationDate;
    public int studentsCount;
    public Long shouldBeExpelled;
    public String formOfEducation;
    public String semesterEnum;
    public PersonDTO groupAdmin;

}


