package storage.exception;

public class RecursionExeption extends Throwable {
    public RecursionExeption(String message) {
        super(message);
    }

    public RecursionExeption(Throwable cause) {
        super(cause);
    }
}
