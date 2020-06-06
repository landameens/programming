package storage.dao;

import storage.dao.exception.DAOException;

import java.util.List;

public interface DAO<T> {
    void create(List<T> entites) throws DAOException;

    void create(T entity) throws DAOException;

    List<T> getAll() throws DAOException;

    T get(Class<? extends T> clazz, long id) throws DAOException;

    void update(T entity) throws DAOException;

    void delete(T entity) throws DAOException;
}
