package domain.userRepository.exception;

public class UserRepositoryException extends Exception {
    public UserRepositoryException() {
    }

    public UserRepositoryException(String message) {
        super(message);
    }

    public UserRepositoryException(Throwable cause) {
        super(cause);
    }
}
