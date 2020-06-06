package controller.migration.commands.exception;

public class CreationException extends Throwable {
    public CreationException(String message) {
        super(message);
    }

    public CreationException(Throwable cause) {
        super(cause);
    }
}
