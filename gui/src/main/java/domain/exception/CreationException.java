package domain.exception;

public class CreationException extends Exception {
    public CreationException(String text) {
        super(text);
    }

    public CreationException(Throwable e) {
        super(e);
    }
}
