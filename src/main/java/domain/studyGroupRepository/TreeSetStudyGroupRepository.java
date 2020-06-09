package domain.studyGroupRepository;

import domain.exception.StudyGroupRepositoryException;
import domain.exception.VerifyException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;
import domain.studyGroupFactory.StudyGroupFactory;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import manager.LogManager;
import storage.collectionInfoDAO.CollectionInfoDAO;
import storage.exception.DAOException;
import storage.studyGroupDAO.IStudyGroupDAO;
import storage.studyGroupDAO.Saveable;
import storage.studyGroupDAO.StudyGroupDAO;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * These class are realize IStudyGroupRepository interface for working with Tree set.
 */
public class TreeSetStudyGroupRepository implements IStudyGroupRepository, Saveable {

    private static final String INTERNAL_ERROR_MESSAGE = "Внутренняя ошибка данные не подходят под описание.";
    private static final String TRYING_ADD_NULL_GROUP_ERROR_MESSAGE = "Ошибка, нельзя добавить null группу.";
    private static final String SUCH_GROUP_EXIST_ERROR_MESSAGE = "Такая группа уже существует.";
    private static final String NO_SUCH_STUDY_GROUP_ERROR_MESSAGE = "Такой группы нет в репозитории.";

    private final StudyGroupFactory studyGroupFactory;
    private final Set<StudyGroup> studyGroups;
    private final IStudyGroupDAO studyGroupDAO;
    private final CollectionInfo collectionInfo;
    private CollectionInfoDAO collectionInfoDAO;

    private String directoryForStoringStudyGroups;
    private String directoryForStoringCollectionInfo;

    private String directoryForAppFiles;

    private static final LogManager LOG_MANAGER = LogManager.createDefault(TreeSetStudyGroupRepository.class);

    public TreeSetStudyGroupRepository(StudyGroupFactory studyGroupFactory, String pathForAppFiles) throws DAOException, VerifyException {
        this.studyGroupFactory = studyGroupFactory;
        this.directoryForAppFiles = pathForAppFiles;

        if (pathForAppFiles == null) {
            LOG_MANAGER.debug("Используется путь по умолчанию.");
            ClassLoader classLoader = TreeSetStudyGroupRepository.class.getClassLoader();
            URL groupsUrl = classLoader.getResource("studyGroups");
            directoryForStoringStudyGroups = groupsUrl.getFile();

            URL infoUrl = classLoader.getResource("info");
            directoryForStoringCollectionInfo = infoUrl.getFile();
        } else {
            LOG_MANAGER.debug("Используется полученный путь.");
            directoryForStoringStudyGroups = pathForAppFiles + "/studyGroups";
            directoryForStoringCollectionInfo = pathForAppFiles + "/info";
        }

        studyGroupDAO = new StudyGroupDAO(directoryForStoringStudyGroups);

        Comparator<StudyGroup> studyGroupComparator = new StudyGroup.StudyGroupComparator();
        File file = new File(directoryForStoringStudyGroups);
        studyGroups = getInitialFiles(file, studyGroupComparator);

        collectionInfo = getInfos(directoryForStoringCollectionInfo);
    }

    private synchronized CollectionInfo getInfos(String path) throws DAOException {
        collectionInfoDAO = new CollectionInfoDAO(path);
        return collectionInfoDAO.getInfos();
    }

    private synchronized Set<StudyGroup> getInitialFiles(File directory,
                                            Comparator<StudyGroup> studyGroupComparator) throws DAOException, VerifyException {
        if (directory.listFiles().length != 0) {
            Set<StudyGroupDTO> studyGroupDTOSet;
            Set<StudyGroup> studyGroups = new TreeSet<>(studyGroupComparator);

            try {
                studyGroupDTOSet = studyGroupDAO.getDTOs();
            } catch (JAXBException e) {
                throw new DAOException(e);
            }

            for (StudyGroupDTO studyGroupDTO : studyGroupDTOSet) {
                studyGroups.add(StudyGroup.getStudyGroup(studyGroupDTO));
            }
            return studyGroups;
        }

        return new TreeSet<>(studyGroupComparator);
    }

    /**
     * Adds a Study Group to the collection using StudyGroupDTO, if it isn't there.
     * If it is in the collection, or it is null, then it isn't added.
     * @param studyGroupDTO
     * @throws StudyGroupRepositoryException
     */
    @Override
    public synchronized void add(StudyGroupDTO studyGroupDTO) throws StudyGroupRepositoryException {

        StudyGroup studyGroup;
        try {
            studyGroup = studyGroupFactory.createNewStudyGroup(studyGroupDTO);
            LOG_MANAGER.debug("New study group was created SUCCESSFUL.");
        } catch (VerifyException e) {
            LOG_MANAGER.errorThrowable("");
            throw new StudyGroupRepositoryException(INTERNAL_ERROR_MESSAGE);
        }

        if (studyGroup == null) {
            throw new StudyGroupRepositoryException(TRYING_ADD_NULL_GROUP_ERROR_MESSAGE);
        }

        if (!studyGroups.add(studyGroup)) {
            throw new StudyGroupRepositoryException(SUCH_GROUP_EXIST_ERROR_MESSAGE);
        }

    }

    /**
     * Method for deleting a Study Group from the collection.
     * @param studyGroup
     * @throws StudyGroupRepositoryException
     */
    @Override
    public synchronized void remove(StudyGroup studyGroup) throws StudyGroupRepositoryException {
        try {
            studyGroupFactory.getIdProducer().clear();
        } catch (DAOException e) {
            throw new StudyGroupRepositoryException(e.getMessage());
        }
        if (!studyGroups.remove(studyGroup)){
            throw new StudyGroupRepositoryException(NO_SUCH_STUDY_GROUP_ERROR_MESSAGE);
        }
    }

    /**
     * Updates Study Group in the collection.
     * By replacing it with a new Study Group.
     * @param studyGroup
     * @throws StudyGroupRepositoryException
     */
    @Override
    public void update(StudyGroup studyGroup) throws StudyGroupRepositoryException {
        StudyGroup studyGroupExist = findStudyGroup(studyGroup);

        if (studyGroupExist == null){
            throw new StudyGroupRepositoryException(NO_SUCH_STUDY_GROUP_ERROR_MESSAGE);
        }

        studyGroups.remove(studyGroupExist);

        if(!studyGroups.add(studyGroup)){
            throw new StudyGroupRepositoryException(INTERNAL_ERROR_MESSAGE);
        }
    }

    private StudyGroup findStudyGroup(StudyGroup studyGroup){
        for (StudyGroup currentStudyGroup : studyGroups){
            if (currentStudyGroup.getId().equals(studyGroup.getId())){
                return currentStudyGroup;
            }
        }
        return null;
    }

    /**
     * Get set of Study Group from the collection.
     * @param concreteSet
     * @return
     * @throws StudyGroupRepositoryException
     */
    @Override
    public synchronized Set<StudyGroup> getConcreteSetOfStudyGroups(ConcreteSet concreteSet) throws StudyGroupRepositoryException{
        if (studyGroups.isEmpty()){
            return new TreeSet<>();
        }

        return concreteSet.execute(studyGroups);
    }

    @Override
    public synchronized CollectionInfo getInfo() {
        collectionInfo.size = studyGroups.size();
        return collectionInfo;
    }

    @Override
    public Set<StudyGroup> getStudyGroup(long id) throws StudyGroupRepositoryException {

        return null;
    }


    @Override
    public synchronized void save() throws DAOException {
        Set<StudyGroupDTO> studyGroupDTOSet = new LinkedHashSet<>();

        for (StudyGroup studyGroup : studyGroups) {
            StudyGroupDTO studyGroupDTO = StudyGroup.getStudyGroupDTO(studyGroup);
            studyGroupDTOSet.add(studyGroupDTO);
        }

        studyGroupDAO.saveDTOs(studyGroupDTOSet);
        collectionInfoDAO.saveInfo(collectionInfo);
        studyGroupFactory.getIdProducer().saveId();
    }

    public String getDirectoryForAppFiles() {
        return directoryForAppFiles;
    }
}
