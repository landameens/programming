package server.view.screen;

import app.Console;
import app.Exceptions.ConsoleException;
import app.controller.services.exitingDirector.INeedExiting;
import controller.Controller;
import controller.exception.ControllerException;
import manager.LogManager;
import query.Query;
import response.Response;
import router.screen.Screen;
import router.screen.ScreenContext;
import router.screen.ScreenMemento;
import server.view.Viewer;

import java.util.HashMap;
import java.util.Map;

public abstract class ServerScreen implements INeedExiting, Screen {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(ServerScreen.class);

    protected final Console console;
    protected final Viewer viewer;
    protected final Controller controller;

    protected boolean isActive;

    protected ScreenContext screenContext;


    public ServerScreen(Console console,
                        Viewer viewer,
                        Controller controller) {
        this.console = console;
        this.viewer = viewer;
        this.controller = controller;
    }

    @Override
    public void onStart(ScreenContext screenContext) {
        isActive = true;

        this.screenContext = screenContext;

        showScreenDescription();

        while (isActive) {
            String userInput = console.readLine();

            if (userInput == null) {
                LOG_MANAGER.debug("User entered null. Asked for repeat.");
                console.writeLine(viewer.showNullEnterInput());
                console.writeLine(viewer.showOfferToRepeatInput());
                continue;
            }

            userInput = userInput.trim();

            Query query = createQuery(userInput);

            Response response;
            try {
                response = controller.handle(query);

                if (response == null) {
                    LOG_MANAGER.debug("Entered unsupported command.");
                    console.writeLine(viewer.showNoSuchCommandErrorMessage());
                    console.writeLine(viewer.showOfferToRepeatInput());
                    continue;
                }

                analyseResponse(response);
            } catch (ControllerException e) {
                handleControllerException(e);
            }
        }
    }

    @Override
    public void onActive(ScreenMemento screenMemento, ScreenContext screenContext) {
        onStart(screenContext);
    }

    protected abstract void showScreenDescription();

    private Query createQuery(String userInput) {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("userInput", userInput);

        return new Query(userInput.split(" +")[0], arguments);
    }

    /**
     * Override it for changing analyse response logic.
     */
    protected abstract void analyseResponse(Response response);

    private void handleControllerException(ControllerException e) throws ConsoleException {
        console.writeLine(viewer.showInternalServerErrorMessage());
    }

    @Override
    public ScreenMemento getState() {
        //This method is not supported for the screen.
        return null;
    }

    @Override
    public void exit() {
        console.writeLine(viewer.showGoodbyeMessage());
        isActive = false;
        System.exit(0);
    }
}
