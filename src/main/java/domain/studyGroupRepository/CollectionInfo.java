package domain.studyGroupRepository;

import storage.adapters.ZonedDateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@XmlRootElement
@XmlType(name = "collectionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public final class CollectionInfo {
    public Class<TreeSetStudyGroupRepository> type;
    @XmlJavaTypeAdapter(ZonedDateTimeAdapter.class)
    public ZonedDateTime creationDate;
    public int size;


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        return "Информация о текущей коллекции:" + System.lineSeparator() +
                "type = " + type.getSimpleName() + System.lineSeparator() +
                "creationDate = " + creationDate.format(formatter) + System.lineSeparator() +
                "size = " + size + System.lineSeparator();
    }
}



