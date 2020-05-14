package domain.studyGroup.coordinates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(name = "Coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
public final class CoordinatesDTO {
    public Integer x;
    public int y;
}
