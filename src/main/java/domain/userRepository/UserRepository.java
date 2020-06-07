package domain.userRepository;

import controller.components.serviceMediator.Service;
import domain.user.User;
import domain.userRepository.exception.UserRepositoryException;
import domain.userRepository.getQuery.GetQuery;

import java.util.List;

public interface UserRepository extends Service {
    void create(User user) throws UserRepositoryException;

    void update(User user) throws UserRepositoryException;

    void delele(User user) throws UserRepositoryException;

    List<User> get(GetQuery getQuery) throws UserRepositoryException;
}
