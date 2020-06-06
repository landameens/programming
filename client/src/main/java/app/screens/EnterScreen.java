package client.view.screens;

import adapter.LoggerAdapter;
import client.view.Console;
import client.view.Viewer;
import controller.Controller;
import response.Response;
import response.Status;
import router.exception.NoSuchRoutException;
import router.screen.ScreenContext;

public final class EnterScreen extends ConsoleScreen {
    private static final LoggerAdapter LOGGER_ADAPTER = LoggerAdapter.createDefault(EnterScreen.class.getSimpleName());


    public EnterScreen(Console console,
                       Viewer viewer,
                       Controller controller) {
        super(console, viewer, controller);
    }

    @Override
    public void onStart(ScreenContext screenContext) {
        isActive = true;
        this.screenContext = screenContext;

        String accessToken = screenContext.get("accessToken");

        if (accessToken != null && !accessToken.isEmpty()) {
            isActive = false;
            try {
                screenContext.getRouter().go("main");
            } catch (NoSuchRoutException e) {
                LOGGER_ADAPTER.errorThrowable("Cannot go to the main scree.", e);
            }
        } else {
            super.onStart(screenContext);
        }
    }

    @Override
    protected void showScreenDescription() {
        console.writeLine(viewer.showGreetingMessage());
    }

    @Override
    protected void analyseResponse(Response response) {
        if (response.getStatus().equals(Status.FOUND)) {
            isActive = false;
            try {
                screenContext.getRouter().go(response.getAnswer());
            } catch (NoSuchRoutException e) {
                LOGGER_ADAPTER.errorThrowable("Cannot go further.", e);
            }
        }

        if (response.getStatus().equals(Status.BAD_REQUEST)) {
            console.writeLine(viewer.showBadRequestErrorMessage());
            console.writeLine(response.getAnswer());
            console.writeLine(viewer.showOfferToRepeatInput());
        }

        console.writeLine();
    }
}
