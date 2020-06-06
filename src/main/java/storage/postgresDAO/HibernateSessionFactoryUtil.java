package storage.postgresDAO;

import domain.studyGroup.StudyGroup;
import domain.studyGroup.person.Person;
import domain.user.User;
import manager.LogManager;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * This is utility class, which has the logic of loading all @Entity classes and configuring Hibernate.
 * It realises singleton.
 * <p>
 * NOTE! Don't forget to close session after opening!
 */
public final class HibernateSessionFactoryUtil {
    private static final LogManager logManager =
            LogManager.createDefault(HibernateSessionFactoryUtil.class);
    private static SessionFactory sessionFactory = null;

    //This is utility class
    private HibernateSessionFactoryUtil() {
    }

    public static SessionFactory getSessionFactory() throws HibernateException {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                logManager.debug("The config of Hibernate loaded SUCCESSFULLY.");

                //adding @Entity classes
                configuration.addAnnotatedClass(StudyGroup.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Person.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                logManager.debug("SessionFactory for Hibernate was created SUCCESSFULLY");
            } catch (Exception e) {
                logManager.errorThrowable("An exception during configuring Hibernate.", e);
                throw new HibernateException(e);
            }
        }

        return sessionFactory;
    }
}
