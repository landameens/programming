package client.view.screens;

import adapter.LoggerAdapter;
import client.controller.services.exitingDirector.INeedExiting;
import client.controller.services.queryBuilder.queryCreationException.InputArgumentsValidationException;
import client.controller.services.queryBuilder.queryCreationException.QueryCreationException;
import client.view.Console;
import client.view.Viewer;
import client.view.exceptions.ConsoleException;
import connection.exception.ConnectionException;
import connection.exception.NotYetConnectedException;
import controller.Controller;
import controller.command.exception.CommandExecutionException;
import controller.exception.ControllerException;
import message.exception.WrongTypeException;
import query.Query;
import response.Response;
import router.exception.NoSuchRoutException;
import router.screen.Screen;
import router.screen.ScreenContext;
import router.screen.ScreenMemento;
import serializer.exception.DeserializationException;
import serializer.exception.SerializationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class defines a "screen" which can be used in the console app.
 * Uses The Router framework
 */
public abstract class ConsoleScreen implements Screen, INeedExiting {
    private static final LoggerAdapter LOGGER_ADAPTER = LoggerAdapter.createDefault(ConsoleScreen.class.getSimpleName());

    protected final Console console;
    protected final Viewer viewer;
    protected final Controller controller;

    protected boolean isActive;

    protected ScreenContext screenContext;



    public ConsoleScreen(Console console,
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
                LOGGER_ADAPTER.debug("User entered null. Asked for repeat.");
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
                    LOGGER_ADAPTER.debug("Entered unsupported command.");
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

    protected void goOnScreen(String rout) {
        try {
            isActive = false;
            screenContext.getRouter().go("login");
        } catch (NoSuchRoutException e) {
            LOGGER_ADAPTER.errorThrowable("Cannot go to the screen with rout: " + rout, e);
        }
    }

    protected abstract void showScreenDescription();

    private Query createQuery(String userInput) {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("userInput", userInput);
        arguments.put("accessToken", screenContext.get("accessToken"));

        return new Query(userInput.split(" +")[0], arguments, null);
    }

    /**
     * Override it for changing analyse response logic.
     */
    protected abstract void analyseResponse(Response response);

    private void handleControllerException(ControllerException e) throws ConsoleException {
        if (e.getCause() instanceof CommandExecutionException) {

            CommandExecutionException commandExecutionException = (CommandExecutionException) e.getCause();

            if (commandExecutionException.getCause() instanceof NotYetConnectedException) {
                console.writeLine(viewer.showNotYetConnectedErrorMessage());
                console.writeLine(viewer.showOfferToRepeatInput());
                return;
            }

            if (commandExecutionException.getCause() instanceof InputArgumentsValidationException) {
                console.writeLine(viewer.getErrorPrefix() + commandExecutionException.getCause().getMessage());
                console.writeLine(viewer.showOfferToRepeatInput());
                return;
            }

            if (commandExecutionException.getCause() instanceof QueryCreationException ||
                    commandExecutionException.getCause() instanceof SerializationException ||
                    commandExecutionException.getCause() instanceof DeserializationException) {
                console.writeLine(viewer.showInternalClientErrorMessage());
                console.writeLine(viewer.showOfferToRepeatInput());
                return;
            }

            if (commandExecutionException.getCause() instanceof WrongTypeException) {
                console.writeLine(viewer.showGetWrongMessageErrorMessage());
                console.writeLine(viewer.showOfferToRepeatInput());
                return;
            }

            if (commandExecutionException.getCause() instanceof ConnectionException) {
                console.writeLine(viewer.showClientCannotConnectToServerErrorMessage());
                console.writeLine(viewer.showOfferToRepeatInput());
                return;
            }

            console.writeLine(viewer.showInternalClientErrorMessage());
            return;
        }

        console.writeLine(viewer.showInternalClientErrorMessage());
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

        try {
            screenContext.save();
        } catch (IOException e) {
            LOGGER_ADAPTER.errorThrowable("Cannot save the shared data,", e);
        }

        System.exit(0);
    }
}
