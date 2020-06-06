package domain.studyGroupRepository;

import domain.exception.StudyGroupRepositoryException;
import domain.exception.VerifyException;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.StudyGroupDTO;
import domain.studyGroupRepository.concreteSet.ConcreteSet;
import manager.LogManager;
import storage.dao.DAO;
import storage.dao.exception.DAOException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DBStudyGroupRepository implements IStudyGroupRepository {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(DBStudyGroupRepository.class);


    private final DAO<StudyGroup> studyGroupDAO;


    public DBStudyGroupRepository(DAO<StudyGroup> studyGroupDAO) {
        this.studyGroupDAO = studyGroupDAO;
    }


    @Override
    public synchronized void add(StudyGroupDTO studyGroupDTO) throws StudyGroupRepositoryException {
        StudyGroup studyGroup;
        try {
            studyGroup = StudyGroup.getStudyGroup(studyGroupDTO);
        } catch (VerifyException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new StudyGroupRepositoryException(e);
        }

        try {
            studyGroupDAO.create(studyGroup);
            LOG_MANAGER.debug("StudyGroup was created: " + studyGroup);
        } catch (DAOException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new StudyGroupRepositoryException(e);
        }
    }

    @Override
    public synchronized void remove(StudyGroup studyGroup) throws StudyGroupRepositoryException {
        try {
            studyGroupDAO.delete(studyGroup);
            LOG_MANAGER.debug("StudyGroup was deleted: " + studyGroup);
        } catch (DAOException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new StudyGroupRepositoryException(e);
        }
    }

    @Override
    public synchronized void update(StudyGroup studyGroup) throws StudyGroupRepositoryException {
        try {
            studyGroupDAO.update(studyGroup);
            LOG_MANAGER.debug("StudyGroup was updated: " + studyGroup);
        } catch (DAOException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new StudyGroupRepositoryException(e);
        }
    }

    @Override
    public synchronized Set<StudyGroup> getConcreteSetOfStudyGroups(ConcreteSet concreteSet) throws StudyGroupRepositoryException {
        List<StudyGroup> studyGroups;
        try {
            studyGroups = studyGroupDAO.getAll();
            LOG_MANAGER.debug("All studyGroups were got: " + studyGroups);
        } catch (DAOException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new StudyGroupRepositoryException(e);
        }

        return concreteSet.execute(new HashSet<>(studyGroups));
    }

    @Override
    public synchronized CollectionInfo getInfo() {
        LOG_MANAGER.warn("Not acceptable");
        return new CollectionInfo();
    }

    @Override
    public synchronized void save() throws storage.exception.DAOException {
        LOG_MANAGER.warn("Not acceptable");
    }
}
