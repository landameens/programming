package controller.serverAdapter.exception;

public class ServerAdapterException extends Exception {
    public ServerAdapterException() {
    }

    public ServerAdapterException(String message) {
        super(message);
    }

    public ServerAdapterException(Throwable cause) {
        super(cause);
    }
}
