package storage.studyGroupDAO;

import domain.studyGroup.StudyGroupDTO;
import storage.exception.DAOException;

import javax.xml.bind.JAXBException;
import java.util.Set;

/**
 * Interface for working in the storage with study group.
 */
public interface IStudyGroupDAO {
    Set<StudyGroupDTO> getDTOs() throws DAOException, JAXBException;

    void saveDTOs(Set<StudyGroupDTO> dto) throws DAOException;
}
