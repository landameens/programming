package domain.userRepository.getQuery;

import domain.user.User;
import domain.userRepository.exception.UserRepositoryException;

import java.util.List;

public abstract class GetQuery {
    public abstract List<User> execute(List<User> users) throws UserRepositoryException;
}
