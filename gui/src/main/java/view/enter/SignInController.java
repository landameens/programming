package view.enter;

import controller.AbstractController;
import controller.localizer.Localizer;
import controller.serverAdapter.ServerAdapter;
import controller.serverAdapter.exception.ServerAdapterException;
import controller.serverAdapter.exception.ServerInternalErrorException;
import controller.serverAdapter.exception.ServerUnavailableException;
import controller.serverAdapter.exception.WrongQueryException;
import domain.user.ServerUserDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import response.Response;
import response.Status;
import view.fxController.FXController;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class SignInController extends FXController {
    private static final String WRONG_LOGIN_OR_PASSWORD = Localizer.getStringFromBundle("wrongLoginOrPassword", "SignInScreen");
    private static final String DISC_FROM_SERVER = Localizer.getStringFromBundle("disconnectFormServer", "SignInScreen");
    private static final String SUCC_REC = Localizer.getStringFromBundle("successfullyReconnected", "SignInScreen");
    private static final String SER_ANS_INT_ERR = Localizer.getStringFromBundle("serverAnswerInternalError", "SignInScreen");

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label loginLabel;

    @FXML
    private Button signInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label errorText;


    private ServerAdapter serverAdapter;
    private ServerUserDAO serverUserDAO;

    public SignInController(AbstractController businessLogicController,
                            ServerAdapter serverAdapter,
                            ServerUserDAO serverUserDAO) {
        super(businessLogicController);
        this.serverAdapter = serverAdapter;
        this.serverUserDAO = serverUserDAO;
    }

    @FXML
    public void initialize() {
        /*Localizer.bindComponentToLocale(welcomeLabel, "SignInScreen", "welcome");
        Localizer.bindComponentToLocale(loginLabel, "SignInScreen", "login");
        Localizer.bindTextFieldToLocale(login, "SignInScreen", "enterLogin");
        Localizer.bindComponentToLocale(passwordLabel, "SignInScreen", "password");
        Localizer.bindTextFieldToLocale(password, "SignInScreen", "enterPassword");
        Localizer.bindComponentToLocale(signInButton, "SignInScreen", "singIn");
        Localizer.bindComponentToLocale(newToStorageLabel, "SignInScreen", "newToStorage");
        Localizer.bindComponentToLocale(signUpButton, "SignInScreen", "singUp");
        Localizer.bindComponentToLocale(exitButton, "SignInScreen", "exit");
*/
    }

    private void showInternalErrorAlert(String string) {
        if (alert == null) {
            alert = new Alert(Alert.AlertType.ERROR, string);
            alert.showAndWait();
            alert = null;
        }
    }

    private Alert alert;

    private void showDisconnectAlert() {
        if (alert != null) {
            return;
        }

        alert = new Alert(Alert.AlertType.CONFIRMATION,
                DISC_FROM_SERVER,
                ButtonType.FINISH, ButtonType.OK);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> reconnectToServer());
            }
        }, 30000);

        Optional<ButtonType> response = alert.showAndWait();
        response.ifPresent(buttonType -> {
            if (buttonType.equals(ButtonType.OK)) {
                alert = null;
                reconnectToServer();
            }

            if (buttonType.equals(ButtonType.FINISH)) {
                alert = null;
                System.exit(0);
            }

            alert = null;
        });
    }

    private void reconnectToServer() {
        try {
            //check for dostupnost'
            serverUserDAO.getAllUser();

            alert = new Alert(Alert.AlertType.INFORMATION, SUCC_REC);
            alert.showAndWait();
            alert = null;
        } catch (ServerAdapterException e) {
            handleServerAdapterException(e);
        }
    }

    private void handleServerAdapterException(ServerAdapterException serverAdapterException) {
        if (serverAdapterException instanceof ServerInternalErrorException) {
            showInternalErrorAlert(SER_ANS_INT_ERR);
            System.exit(1);
        }

        if (serverAdapterException instanceof ServerUnavailableException) {
            showDisconnectAlert();
        }

        if (serverAdapterException instanceof WrongQueryException) {
            errorText.setText(WRONG_LOGIN_OR_PASSWORD);
        }
    }

    @FXML
    private void signInAction() {
        String userInput = String.format("login %1$s %2$s", login.getText(), password.getText());

        Response response;
        try {
            response = delegateToBusinessLogicController(userInput);
        } catch (ServerAdapterException e) {
            handleServerAdapterException(e);
            return;
        }

        if (response.getStatus().equals(Status.SUCCESSFULLY)) {
            errorText.setText("");
            String[] strings = response.getAnswer().split(" +");
            screenContext.add("login", strings[0]);
            screenContext.add("password", strings[1]);
            serverAdapter.setLogin(strings[0]);
            serverAdapter.setPassword(strings[1]);
            screenContext.getRouter().go("main");
        }
    }

    @FXML
    private void signUpAction() {
        screenContext.getRouter().go("signUp");
    }

    @Override
    public void onStart() {
        sceneAdapter.getStage().setFullScreen(false);
    }
}
