package app;

import app.Exceptions.InputException;
import manager.LogManager;

public final class App {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(App.class);

    public static void main(String[] args) {
        Console console = new Console(System.in, System.out, false);
        LOG_MANAGER.debug("Console was created SUCCESSFUL");

        try {
            LOG_MANAGER.info("Console is starting...");
            console.start();
        } catch (InputException e) {
            LOG_MANAGER.fatalThrowable(e);
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
