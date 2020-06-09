package domain.userRepository.getQuery;

import domain.user.User;
import domain.userRepository.exception.UserRepositoryException;

import java.util.ArrayList;
import java.util.List;

public final class GetAllQuery extends GetQuery {
    @Override
    public List<User> execute(List<User> users) throws UserRepositoryException {
        List<User> result = new ArrayList<>();
        users.forEach(user -> {
            User clone = user.clone();
            clone.setPassword("");
            result.add(clone);
        });

        return result;
    }
}
