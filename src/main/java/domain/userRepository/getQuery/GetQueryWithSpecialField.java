package domain.userRepository.getQuery;

import domain.studyGroup.StudyGroup;
import domain.user.User;
import domain.userRepository.exception.UserRepositoryException;
import manager.LogManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation ConcreteSet to get a set of Study Groups which have equals field.
 */
public final class GetQueryWithSpecialField extends GetQuery {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(GetQueryWithSpecialField.class);


    private final String field;
    private final Object value;
    private final Class<?> clazz;

    public GetQueryWithSpecialField(Class<?> clazz,
                                    String field,
                                    Object value){
        this.field = field;
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public List<User> execute(List<User> users) throws UserRepositoryException {
        Field clazzField;
        try {
            clazzField = clazz.getDeclaredField(field);
            clazzField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new UserRepositoryException(e);
        }

        List<User> result = new ArrayList<>();

        try {
            for (User user : users) {
                if (clazzField.get(user).equals(value)) {
                    result.add(user);
                }
            }
        } catch (IllegalAccessException e) {
            LOG_MANAGER.errorThrowable(e);
            throw new UserRepositoryException(e);
        }

        clazzField.setAccessible(false);

        return result;
    }
}
