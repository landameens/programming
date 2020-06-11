package view.enter;

import controller.AbstractController;
import controller.localizer.Localizer;
import controller.serverAdapter.ServerAdapter;
import controller.serverAdapter.exception.ServerAdapterException;
import controller.serverAdapter.exception.ServerInternalErrorException;
import controller.serverAdapter.exception.ServerUnavailableException;
import controller.serverAdapter.exception.WrongQueryException;
import controller.validators.Validator;
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

public class SignUpController extends FXController {
    private static final String WRONG_LOGIN_OR_PASSWORD = Localizer.getStringFromBundle("wrongLoginOrPassword", "SignInScreen");
    private static final String DISC_FROM_SERVER = Localizer.getStringFromBundle("disconnectFormServer", "SignInScreen");
    private static final String SUCC_REC = Localizer.getStringFromBundle("successfullyReconnected", "SignInScreen");
    private static final String SER_ANS_INT_ERR = Localizer.getStringFromBundle("serverAnswerInternalError", "SignInScreen");

    private final Validator validator;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField repeatedPassword;

    @FXML
    private Label createAcc;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button signUpButton;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label repeatPassword;

    @FXML
    private Label passwordLabel;

    @FXML
    private Button goBackButton;

    @FXML
    private Label errorText;


    private ServerUserDAO serverUserDAO;
    private ServerAdapter serverAdapter;

    public SignUpController(AbstractController businessLogicController,
                            Validator validator,
                            ServerUserDAO serverUserDAO,
                            ServerAdapter serverAdapter) {
        super(businessLogicController);
        this.validator = validator;
        this.serverUserDAO = serverUserDAO;
        this.serverAdapter = serverAdapter;
    }

    @FXML
    public void initialize() {
        Localizer.bindComponentToLocale(createAcc, "SignUpScreen", "createYourAccount");
        Localizer.bindComponentToLocale(usernameLabel, "SignUpScreen", "username");
        Localizer.bindComponentToLocale(repeatPassword, "SignUpScreen", "repeatPassword");
        Localizer.bindComponentToLocale(goBackButton, "SignUpScreen", "goBack");
        Localizer.bindComponentToLocale(signUpButton, "SignUpScreen", "singUp");
        Localizer.bindComponentToLocale(passwordLabel, "SignUpScreen", "password");
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
            showInternalErrorAlert(SER_ANS_INT_ERR);
        }
    }

    @FXML
    private void signUpAction() {
        String validateAnswer = validator.validateFields(username.getText(), password.getText(), repeatedPassword.getText());
        if (!validateAnswer.equals("")) {
            errorText.setText(validateAnswer);
            return;
        }
        errorText.setText(validateAnswer);

        String userInput = String.format("signUp %1$s %2$s", username.getText(), password.getText());
        Response response = null;
        try {
            response = delegateToBusinessLogicController(userInput);
        } catch (ServerAdapterException e) {
            handleServerAdapterException(e);
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
    private void goBackAction() {
        screenContext.getRouter().goBack();
    }

    @Override
    public void onStart() {
        sceneAdapter.getStage().setFullScreen(false);
    }
}
