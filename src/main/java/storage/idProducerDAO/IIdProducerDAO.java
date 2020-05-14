package storage.idProducerDAO;

import domain.studyGroupFactory.idProducer.IdProducerDTO;
import storage.exception.DAOException;

/**
 * Interface for working with IdProducer in the memory.
 */
public interface IIdProducerDAO {
    IdProducerDTO getIdProducerDTO() throws DAOException;

    void saveIdProducerDTO(IdProducerDTO dto) throws DAOException;
}
