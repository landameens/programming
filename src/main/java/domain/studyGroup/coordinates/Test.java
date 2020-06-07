package domain.studyGroup.coordinates;

import domain.user.User;
import storage.dao.DAO;
import storage.dao.exception.DAOException;
import storage.postgresDAO.PostgresDAO;

public class Test {
    public static void main(String[] args) throws DAOException {
        User user = new User("ssd", "sdsd");

        DAO<User> userPostgresDAO = new PostgresDAO<>("From user");

        userPostgresDAO.create(user);
    }
}
