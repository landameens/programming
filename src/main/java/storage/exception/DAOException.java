package storage.exception;

public class DAOException extends Exception {
    public DAOException(String text) {
        super(text);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
