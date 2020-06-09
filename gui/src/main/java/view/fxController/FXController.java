package view.fxController;

import controller.AbstractController;
import controller.command.exception.CommandExecutionException;
import controller.exception.ControllerException;
import controller.serverAdapter.exception.ServerAdapterException;
import javafx.scene.control.Alert;
import query.Query;
import response.Response;
import router.screen.ScreenContext;
import view.sceneAdapter.SceneAdapter;

import java.util.HashMap;
import java.util.Map;

public abstract class FXController {
    protected SceneAdapter sceneAdapter;
    protected ScreenContext screenContext;
    private final AbstractController businessLogicController;


    public FXController(AbstractController businessLogicController) {
        this.businessLogicController = businessLogicController;
    }


    public void setSceneAdapter(SceneAdapter sceneAdapter) {
        this.sceneAdapter = sceneAdapter;
    }

    public void setScreenContext(ScreenContext screenContext) {
        this.screenContext = screenContext;
    }


    protected final Response delegateToBusinessLogicController(String userInput) throws ServerAdapterException {
        Query query = createQueryToBusinessLogicController(userInput);

        Response response = null;
        try {
            response = businessLogicController.handle(query);
        } catch (ControllerException e) {
            handleBusinessLogicControllerException(e);
        }

        if (response == null) {
            throw new RuntimeException("Пс, парень, чекни конфигурацию бизнес-контроллера.");
        }

        return response;
    }

    private Query createQueryToBusinessLogicController(String userInput) {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("userInput", userInput);
        arguments.put("login", screenContext.get("login"));
        arguments.put("password", screenContext.get("password"));

        return new Query(userInput.split(" +")[0], arguments);
    }

    private void handleBusinessLogicControllerException(ControllerException e) throws ServerAdapterException {
        if (e.getCause() instanceof CommandExecutionException) {

            CommandExecutionException commandExecutionException = (CommandExecutionException) e.getCause();

            if (commandExecutionException.getCause() instanceof ServerAdapterException) {
                throw (ServerAdapterException) commandExecutionException.getCause();
            }

            throw new RuntimeException("Проблема в обработке ошибок в FXController");
        }
    }

    protected void showErrorAlert(String errorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Storage");
        alert.setContentText(errorText);
        alert.show();
    }

    public void onStart() {

    }
}
