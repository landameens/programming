package storage.postgresDAO;

import manager.LogManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import storage.dao.DAO;
import storage.dao.exception.DAOException;

import java.util.List;

/**
 * Actually, to save and load data to DB, we don't use DTOs
 *
 * @param <T>
 */
public final class PostgresDAO<T> implements DAO<T> {
    private final LogManager logManager;
    private final String stringHQL;


    public PostgresDAO(String hqlString) {
        stringHQL = hqlString;
        logManager = LogManager.createDefault(PostgresDAO.class);
    }

    @Override
    public void create(List<T> entites) throws DAOException {
        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        logManager.debug("SessionFactory created SUCCESSFULLY");


        try (Session session = sessionFactory.openSession()) {
            logManager.debug("The session with DB opened SUCCESSFULLY");

            Transaction transaction = session.beginTransaction();
            logManager.debug("The transaction began SUCCESSFULLY");

            entites.forEach(session::save);
            transaction.commit();
            logManager.debug("All data was saved SUCCESSFULLY");
        } catch (HibernateException e) {
            logManager.errorThrowable("Hibernate cannot save DTOs", e);
            throw new DAOException(e);
        }
    }

    @Override
    public void create(T entity) throws DAOException {
        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        logManager.debug("SessionFactory created SUCCESSFULLY");


        try (Session session = sessionFactory.openSession()) {
            logManager.debug("The session with DB opened SUCCESSFULLY");

            Transaction transaction = session.beginTransaction();
            logManager.debug("The transaction began SUCCESSFULLY");

            session.save(entity);
            transaction.commit();
            logManager.debug("All data was saved SUCCESSFULLY");
        } catch (HibernateException e) {
            logManager.errorThrowable("Hibernate cannot save DTOs", e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<T> getAll() throws DAOException {
        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        logManager.debug("SessionFactory created SUCCESSFULLY");

        List<T> dtos = null;
        try (Session session = sessionFactory.openSession()) {
            logManager.debug("The session with DB opened SUCCESSFULLY");

            dtos = session.createQuery(stringHQL).list();
            logManager.debug("All data was loaded from DB SUCCESSFULLY");
        } catch (HibernateException e) {
            logManager.errorThrowable("Hibernate cannot load DTOs", e);
            throw new DAOException(e);
        }

        return dtos;
    }

    @Override
    public T get(Class<? extends T> clazz, long id) throws DAOException {
        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        logManager.debug("SessionFactory created SUCCESSFULLY");

        T entity = null;

        try (Session session = sessionFactory.openSession()) {
            logManager.debug("The session with DB opened SUCCESSFULLY");

            Transaction transaction = session.beginTransaction();
            logManager.debug("The transaction began SUCCESSFULLY");

            entity = session.get(clazz, id);

            transaction.commit();
            logManager.debug("User was updated SUCCESSFULLY");
        } catch (HibernateException e) {
            logManager.errorThrowable("Hibernate cannot update DTOs", e);
            throw new DAOException(e);
        }

        return entity;
    }

    @Override
    public void update(T entity) throws DAOException {
        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        logManager.debug("SessionFactory created SUCCESSFULLY");

        try (Session session = sessionFactory.openSession()) {
            logManager.debug("The session with DB opened SUCCESSFULLY");

            Transaction transaction = session.beginTransaction();
            logManager.debug("The transaction began SUCCESSFULLY");

            session.update(entity);

            transaction.commit();
            logManager.debug("User was updated SUCCESSFULLY");
        } catch (HibernateException e) {
            logManager.errorThrowable("Hibernate cannot update DTOs", e);
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(T entity) throws DAOException {
        SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();
        logManager.debug("SessionFactory created SUCCESSFULLY");

        try (Session session = sessionFactory.openSession()) {
            logManager.debug("The session with DB opened SUCCESSFULLY");

            Transaction transaction = session.beginTransaction();
            logManager.debug("The transaction began SUCCESSFULLY");

            session.delete(entity);
            transaction.commit();
            logManager.debug("User was deleted SUCCESSFULLY");
        } catch (HibernateException e) {
            logManager.errorThrowable("Hibernate cannot delete DTOs", e);
            throw new DAOException(e);
        }
    }
}
