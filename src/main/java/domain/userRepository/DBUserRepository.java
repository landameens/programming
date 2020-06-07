package domain.userRepository;

import domain.user.User;
import domain.userRepository.exception.UserRepositoryException;
import domain.userRepository.getQuery.GetQuery;
import manager.LogManager;
import storage.dao.DAO;
import storage.dao.exception.DAOException;

import java.util.List;

public final class DBUserRepository implements UserRepository {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(DBUserRepository.class);


    private DAO<User> userDAO;


    public DBUserRepository(DAO<User> userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void create(User user) throws UserRepositoryException {
        try {
            userDAO.create(user);
            LOG_MANAGER.debug("User was created: " + user);
        } catch (DAOException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new UserRepositoryException(e);
        }
    }

    @Override
    public void update(User user) throws UserRepositoryException {
        try {
            userDAO.update(user);
            LOG_MANAGER.debug("User was updated: " + user);
        } catch (DAOException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new UserRepositoryException(e);
        }
    }

    @Override
    public void delele(User user) throws UserRepositoryException {
        try {
            userDAO.delete(user);
            LOG_MANAGER.debug("User was deleted: " + user);
        } catch (DAOException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new UserRepositoryException(e);
        }
    }

    @Override
    public List<User> get(GetQuery getQuery) throws UserRepositoryException {
        List<User> users;
        try {
            users = userDAO.getAll();
            LOG_MANAGER.debug("All user were got: " + users);
        } catch (DAOException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new UserRepositoryException(e);
        }
        return getQuery.execute(users);
    }
}
