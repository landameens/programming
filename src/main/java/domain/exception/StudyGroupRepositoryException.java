package domain.exception;

public class StudyGroupRepositoryException extends Exception {
    public StudyGroupRepositoryException(String text) {
            super(text);
        }

    public StudyGroupRepositoryException(Throwable cause) {
        super(cause);
    }
}
