package domain.studyGroup.person;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
public final class PersonDTO {
    public String name;
    public int height;
    public String passportID;
    public String nationality;

}
