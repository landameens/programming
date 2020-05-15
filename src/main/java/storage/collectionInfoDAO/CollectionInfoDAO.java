package storage.collectionInfoDAO;

import domain.studyGroupRepository.CollectionInfo;
import domain.studyGroupRepository.TreeSetStudyGroupRepository;
import storage.exception.DAOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * These class realize ICollectionInfoDAO.
 * It uses for working with Collection Info in the storage.
 */
public class CollectionInfoDAO implements ICollectionInfoDAO {
    private String pathToInfo;

    public CollectionInfoDAO(String pathToFile) {
        this.pathToInfo = pathToFile;
    }

    /**
     * This method decerialize xml-file with infos from storage.
     * @return collectionInfo
     * @throws DAOException
     */
    @Override
    public CollectionInfo getInfos() throws DAOException {
        File file = new File(pathToInfo);

        if (!file.exists()) {
            file.mkdir();
        }
        File[] files =  file.listFiles();

        List<File> collectionInfo = Arrays.asList(files);
        return deserialize(collectionInfo);
    }

    private CollectionInfo deserialize(List<File> collectionInfo)throws DAOException{

        for (File file : collectionInfo) {
            try {
                JAXBContext context = JAXBContext.newInstance(CollectionInfo.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (CollectionInfo) unmarshaller.unmarshal(file);
            } catch (JAXBException e) {
                throw new DAOException(e);
            }
        }

        return createCollectionInfo();
    }

    private CollectionInfo createCollectionInfo(){
        CollectionInfo info = new CollectionInfo();
        info.creationDate = ZonedDateTime.now();
        info.size = 0;
        info.type = TreeSetStudyGroupRepository.class;

        return info;
    }

    /**
     * This method writes Collection Info to xml-file in the memory.
     * @param collectionInfo
     * @throws DAOException
     */
    @Override
    public void saveInfo(CollectionInfo collectionInfo) throws DAOException {
        File file = new File(pathToInfo.concat("\\info.xml"));
        file.delete();
        File file1 = new File(pathToInfo.concat("\\info.xml"));
        serialize(file1, collectionInfo);
    }

    private void serialize(File file, CollectionInfo collectionInfo) throws DAOException {
        try {
            JAXBContext context = JAXBContext.newInstance(CollectionInfo.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(collectionInfo, file);
        } catch (JAXBException e) {
            throw new DAOException(e);
        }
    }
}
