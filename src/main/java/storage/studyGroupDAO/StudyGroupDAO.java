package storage.studyGroupDAO;

import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;
import storage.exception.DAOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

/**
 * These class realize IStudyGroupDAO.
 * It uses for working with Study Groups in the storage.
 */
public class StudyGroupDAO implements IStudyGroupDAO {
    private final static String EMPTY_DIRECTORY_ERROR = "Отсутствуют файлы.";

    private File file;

    public StudyGroupDAO(String pathToFile) {
        this.file = new File(pathToFile);

        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * This method decerialize xml-file with Study groups from storage.
     * @return Set of Study Group
     * @throws DAOException
     */
    @Override
    public Set<StudyGroupDTO> getDTOs() throws DAOException {
        File[] files =  file.listFiles();

        if (files == null){
            throw new DAOException(EMPTY_DIRECTORY_ERROR);
        }

        List<File> studyGroupFiles = Arrays.asList(files);
        return deserialize(studyGroupFiles);
    }

    private Set<StudyGroupDTO> deserialize(List<File> studyGroupFiles)throws DAOException{
        Comparator<StudyGroupDTO> studyGroupDTOComparator = new StudyGroup.StudyGroupDTOComparator();
        Set<StudyGroupDTO> studyGroupDTOSet = new TreeSet<>(studyGroupDTOComparator);
        for (File studyGroupFile : studyGroupFiles) {
            try {
                JAXBContext context = JAXBContext.newInstance(StudyGroupDTO.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                StudyGroupDTO studyGroupDTO = (StudyGroupDTO) unmarshaller.unmarshal(studyGroupFile);
                studyGroupDTOSet.add(studyGroupDTO);
            } catch (JAXBException e) {
                throw new DAOException(e);
            }
        }
        return studyGroupDTOSet;
    }

    /**
     * This method writes all study group to xml-file in the memory.
     * @param dto
     * @throws DAOException
     */
    @Override
    public void saveDTOs(Set<StudyGroupDTO> dto) throws DAOException {
        String path = file.getPath();
        File[] oldfiles =  file.listFiles();

        List<File> studyGroupFiles = Arrays.asList(oldfiles);
        for (File studyGroupFile : studyGroupFiles) {
            studyGroupFile.delete();
        }

        List<StudyGroupDTO> studyGroupDTOs = new ArrayList<>(dto);
        List<String> names = fillListByNames(studyGroupDTOs, path);
        List<File> files = createFiles(names);
        serialize(dto, files);
    }

    private List<String> fillListByNames(List<StudyGroupDTO> studyGroupDTOs, String path){

        List<String> names = new LinkedList<>();
        for (StudyGroupDTO studyGroupDTO : studyGroupDTOs){
            names.add(path + "//StudyGroup" + studyGroupDTO.id + ".xml");
        }
        return names;
    }

    private List<File> createFiles(List<String> names){
        List<File> files = new ArrayList<>();
        for (String name : names){
            File file = new File(name);
            files.add(file);
        }
        return files;
    }

    private void serialize(Set<StudyGroupDTO> studyGroupDTOs, List<File> files) throws DAOException {
        for (StudyGroupDTO studyGroupDTO : studyGroupDTOs) {
            try {
                JAXBContext context = JAXBContext.newInstance(StudyGroupDTO.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.marshal(studyGroupDTO, files.get(0));
                files.remove(0);
            } catch (JAXBException e) {
                throw new DAOException(e);
            }
        }
    }
}
