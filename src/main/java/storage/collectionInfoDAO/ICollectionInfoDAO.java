package storage.collectionInfoDAO;

import domain.studyGroupRepository.CollectionInfo;
import storage.exception.DAOException;

import javax.xml.bind.JAXBException;

/**
 * Interface for working in the memory with collection info.
 */
public interface ICollectionInfoDAO {
    CollectionInfo getInfos() throws DAOException, JAXBException;

    void saveInfo(CollectionInfo collectionInfo) throws DAOException;
}
