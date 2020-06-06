package storage.dao;

import storage.dao.exception.DAOException;

import java.util.List;

public interface DAO<T> {
    void create(List<T> entites) throws DAOException;

    void create(T entity) throws DAOException;

    List<T> getAll() throws DAOException;

    T get(long id) throws DAOException;

    void update(T entity) throws DAOException;

    void delete(long id) throws DAOException;
}
