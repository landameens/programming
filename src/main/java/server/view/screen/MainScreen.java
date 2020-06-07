package server.view.screen;

import app.Console;
import controller.Controller;
import manager.LogManager;
import response.Response;
import response.Status;
import server.view.Viewer;

public final class MainScreen extends ServerScreen {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(MainScreen.class);


    public MainScreen(Console console, Viewer viewer, Controller controller) {
        super(console, viewer, controller);
    }


    @Override
    protected void showScreenDescription() {
        console.writeLine("Here you can manage your collection." + System.lineSeparator() + "Available commands: exit");
    }


    protected void analyseResponse(Response response) {
        LOG_MANAGER.debug("Controller returned: " + response);


        if (response.getStatus().equals(Status.BAD_REQUEST)) {
            console.writeLine(viewer.showBadRequestErrorMessage());
            console.writeLine(viewer.showControllerPrefixAnswer() + response.getAnswer());
            console.writeLine(viewer.showOfferToRepeatInput());
        }

        if (response.getStatus().equals(Status.SUCCESSFULLY)) {
            console.writeLine(response.getAnswer());
        }

        if (response.getStatus().equals(Status.INTERNAL_ERROR)) {
            console.writeLine(viewer.showInternalServerErrorMessage());
            console.writeLine(viewer.showOfferToRepeatInput());
        }

        console.writeLine();
    }
}
