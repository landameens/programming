package client.view.screens;

import adapter.LoggerAdapter;
import client.view.Console;
import client.view.Viewer;
import controller.Controller;
import controller.exception.ControllerException;
import query.Query;
import response.Response;
import response.Status;
import router.screen.ScreenContext;

import java.util.HashMap;
import java.util.Map;

public final class MainScreen extends ConsoleScreen {
    private static final LoggerAdapter LOGGER_ADAPTER = LoggerAdapter.createDefault(MainScreen.class.getSimpleName());


    public MainScreen(Console console, Viewer viewer, Controller controller) {
        super(console, viewer, controller);
    }


    @Override
    public void onStart(ScreenContext screenContext) {
        isActive = true;
        this.screenContext = screenContext;

        Response response = sendSessionValidationQuery();

        if (response == null) {
            doIfServerUnavailable();
            return;
        }

        if (response.getStatus().equals(Status.FORBIDDEN)) {
            doIfAccessTokenExpired();
            return;
        }

        super.onStart(screenContext);
    }

    private Response sendSessionValidationQuery() {
        String userInput = "teapot";

        Map<String, String> arguments = new HashMap<>();
        arguments.put("userInput", userInput);
        arguments.put("accessToken", screenContext.get("accessToken"));

        Query query = new Query(userInput.split(" +")[0], arguments, null);

        try {
            return controller.handle(query);
        } catch (ControllerException e) {
            LOGGER_ADAPTER.errorThrowable("Cannot send the session validation query.", e);
            return null;
        }
    }

    private void doIfServerUnavailable() {
        console.writeLine("Server is unavailable. Please, try later.");
        goOnScreen("enter");
    }

    private void doIfAccessTokenExpired() {
        console.writeLine("Server answered that your session is expired. Please, login.");
        screenContext.remove("accessToken");
        goOnScreen("login");
    }

    @Override
    protected void showScreenDescription() {
        console.writeLine("Now you can work with collection. For details, please, enter \"help\" command.");
    }


    protected void analyseResponse(Response response) {
        LOGGER_ADAPTER.debug("Server answered: " + response);

        if (response.getStatus().equals(Status.FORBIDDEN)) {
            console.writeLine("Server answered that your session is expired. Please login.");
            screenContext.remove("accessToken");
            goOnScreen("login");
        }

        if (response.getStatus().equals(Status.BAD_REQUEST)) {
            console.writeLine(viewer.showBadRequestErrorMessage());
            console.writeLine(viewer.showPrefixServerAnswer() + response.getAnswer());
            console.writeLine(viewer.showOfferToRepeatInput());
        }

        if (response.getStatus().equals(Status.SUCCESSFULLY)) {
            console.writeLine(response.getAnswer());
        }

        if (response.getStatus().equals(Status.INTERNAL_ERROR)) {
            console.writeLine(viewer.showInternalServerErrorMessage());
            console.writeLine(viewer.showOfferToRepeatInput());
        }

        if (response.getStatus().equals(Status.PRECONDITION_FAILED)) {
            console.writeLine(viewer.showUnsuccessfulCommandMessage());
            console.writeLine(viewer.showPrefixServerAnswer() + response.getAnswer());
            console.writeLine(viewer.showOfferToRepeatInput());
        }

        if (response.getStatus().equals(Status.GO_BACK)) {
            screenContext.remove("accessToken");
            goOnScreen("enter");
        }

        console.writeLine();
    }
}
