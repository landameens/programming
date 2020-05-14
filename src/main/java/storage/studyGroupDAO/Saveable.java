package storage.studyGroupDAO;

import storage.exception.DAOException;

public interface Saveable {
    void save() throws DAOException;
}
