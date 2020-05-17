package app;

import app.Exceptions.InputException;
import manager.LogManager;
import server.Server;

public final class App {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(App.class);

    public static void main(String[] args) {
        Console console = new Console(System.in, System.out);
        LOG_MANAGER.debug("Console was created SUCCESSFULL");

        Server server = new Server(8010, 128);
        server.start(args);

        try {
            LOG_MANAGER.info("App is starting...");
            console.start();
        } catch (InputException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
