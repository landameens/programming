package storage.idProducerDAO;

import domain.studyGroupFactory.idProducer.IdProducerDTO;
import storage.exception.DAOException;

import java.io.*;

/**
 * Class for writing and reading the IdProducer state
 */
public class IdProducerDAO implements IIdProducerDAO {
    private File directoryForStoringFiles;

    public IdProducerDAO(String path) {
        directoryForStoringFiles = new File(path);

        if (!directoryForStoringFiles.exists()) {
            directoryForStoringFiles.mkdir();
        }
    }

    /**
     * This method reads DTO from the memory.
     * @return IdProducerDTO
     * @throws DAOException
     */
    @Override
    public IdProducerDTO getIdProducerDTO() throws DAOException{
        File sourceFile = getSourceFile(directoryForStoringFiles);

        if(sourceFile == null){
            return null;
        }

        try(ObjectInput objectInput = new ObjectInputStream(new FileInputStream(sourceFile))){
            return (IdProducerDTO) objectInput.readObject();
        } catch (IOException | ClassNotFoundException e){
            throw new DAOException(e);
        }
    }

    private File getSourceFile(File directoryForStoringFiles){
        File[] files = directoryForStoringFiles.listFiles();
        if (files.length == 0){
            return null;
        }

        return files[0];
    }

    /**
     * This method saves DTO on the memory.
     * @param dto
     * @throws DAOException
     */
    @Override
    public void saveIdProducerDTO(IdProducerDTO dto) throws DAOException {
        cleanDirectory();

        File sourceFile = new File(directoryForStoringFiles + "\\idProducer.txt");
        try(ObjectOutput objectOutput = new ObjectOutputStream((new FileOutputStream(sourceFile)))) {
            objectOutput.writeObject(dto);
        } catch (IOException e) {
            throw new DAOException(e);
        }
    }

    private void cleanDirectory() {
        File[] files = directoryForStoringFiles.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files){
            file.delete();
        }
    }
}