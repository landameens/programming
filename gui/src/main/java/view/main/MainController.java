package view.main;

import controller.AbstractController;
import controller.localizer.Localizer;
import controller.serverAdapter.exception.*;
import domain.studyGroup.FormOfEducation;
import domain.studyGroup.Semester;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.dao.ServerStudyGroupDAO;
import domain.studyGroup.person.Country;
import domain.studyGroupRepository.StudyGroupCollectionUpdater;
import domain.studyGroupRepository.StudyGroupRepositorySubscriber;
import domain.user.ServerUserDAO;
import domain.user.User;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import manager.LogManager;
import view.fxController.FXController;

import java.util.*;

public class MainController extends FXController implements StudyGroupRepositorySubscriber {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(MainController.class);

    private static final String ENTER_VALUE_TO_FILTER = "Enter value to filter";
    private static final String MENU = "Menu";
    private static final String STUDY_GROUP = "Study group";
    private static final String USER_ID = "User ID";
    private static final String ID = "ID";
    private static final String COORDINATES = "Coordinates";
    private static final String NAME = "Name";
    private static final String CREATION_DATE = "Creation date";
    private static final String STUD_COUNT = "Students count";
    private static final String SHOULD_BE_EXPELLED = "Should be expelled";
    private static final String FORM_OF_EDUCATION = "Form of education";
    private static final String SEMESTER = "Semester";
    private static final String PERSON = "Person";
    private static final String HEIGHT = "Height";
    private static final String PASSPORT_ID = "Passport ID";
    private static final String NATIONALITY = "Nationality";
    @FXML
    public MenuBar menuBar;
    @FXML
    public TextField filter;
    @FXML
    public ChoiceBox<String> choice;
    @FXML
    public TableColumn<StudyGroup, Integer> studyGroup;
    @FXML
    public TableColumn<StudyGroup, Long> idCol;
    @FXML
    public TableColumn<StudyGroup, Integer> userIdCol;
    @FXML
    public TableColumn<StudyGroup, String> nameCol;
    @FXML
    public TableColumn<StudyGroup, Integer> coordinatesCol;
    @FXML
    public TableColumn<StudyGroup, Integer> xCoorCol;
    @FXML
    public TableColumn<StudyGroup, Double> yCoorCol;
    @FXML
    public TableColumn<StudyGroup, String> creatDateCol;
    @FXML
    public TableColumn<StudyGroup, Integer> studCountCol;
    @FXML
    public TableColumn<StudyGroup, Long> shouldBeExpCol;
    @FXML
    public TableColumn<StudyGroup, FormOfEducation> formOfEducCol;
    @FXML
    public TableColumn<StudyGroup, Semester> semesterCol;
    @FXML
    public TableColumn<StudyGroup, Integer> heightCol;
    @FXML
    public TableColumn<StudyGroup, String> passportIdCol;
    @FXML
    public TableColumn<StudyGroup, Country> natCol;
    @FXML
    public TableView<StudyGroup> table;
    @FXML
    public Canvas canvasField;

    private ObservableList<StudyGroup> products;
    private ServerUserDAO serverUserDAO;
    private ServerStudyGroupDAO serverStudyGroupDAO;
    private StudyGroupCollectionUpdater studyGroupCollectionUpdater;


    public MainController(AbstractController businessLogicController,
                          ServerStudyGroupDAO serverStudyGroupDAO,
                          ServerUserDAO serverUserDAO) {

        super(businessLogicController);
        this.serverUserDAO = serverUserDAO;
        this.serverStudyGroupDAO = serverStudyGroupDAO;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * <p>
     * Initializes the table columns and sets up sorting and filtering.
     */
    @FXML
    private void initialize() {
        initTableProperties();
        //bindColumnsToProductFields();

        /*Localizer.bindComponentToLocale(hasLocationButton, "TableScreen", "availabilityLocation");
        Localizer.bindComponentToLocale(hasOrganizationButton, "TableScreen", "availabilityOrganization");
        Localizer.bindComponentToLocale(filter, "TableScreen", "filter");
        Localizer.bindComponentToLocale(productProps, "TableScreen", "prodProps");

        Localizer.bindComponentToLocale(userIdColumn, "TableScreen", "userId");
        Localizer.bindComponentToLocale(productNameColumn, "TableScreen", "name");
        Localizer.bindComponentToLocale(priceColumn, "TableScreen", "price");
        Localizer.bindComponentToLocale(partNumberColumn, "TableScreen", "partNumber");
        Localizer.bindComponentToLocale(unitOfMeasureColumn, "TableScreen", "unitOfMeasure");
        Localizer.bindComponentToLocale(creationDateColumn, "TableScreen", "creationDate");
        Localizer.bindComponentToLocale(manufactureCostColumn, "TableScreen", "manufactureCost");
        Localizer.bindComponentToLocale(organizationColumn, "TableScreen", "organization");
        Localizer.bindComponentToLocale(productColumn, "TableScreen", "product");
        Localizer.bindComponentToLocale(addressColumn, "TableScreen", "address");
        Localizer.bindComponentToLocale(locationColumn, "TableScreen", "location");
        Localizer.bindComponentToLocale(orgNameColumn, "TableScreen", "name");
        Localizer.bindComponentToLocale(orgAnnualTurnoverColumn, "TableScreen", "anTur");
        Localizer.bindComponentToLocale(orgTypeColumn, "TableScreen", "type");
        Localizer.bindComponentToLocale(zipCodeColumn, "TableScreen", "zipCode");
        Localizer.bindComponentToLocale(coordinatesColumn, "TableScreen", "coordinates");*/
    }

    private Timer canvasTimer;
    @Override
    public void onStart() {
        sceneAdapter.getStage().setFullScreen(true);
        // productCollectionUpdater.start();

        // initProductCollection();

        /*try {
            user = serverUserDAO.fromAccessToken(screenContext.get("accessToken"));

            if (user == null) {
                showAccessTokenExpiredAlert();
            }
        } catch (ServerAdapterException e) {
            handleServerAdapterException(e);
        }

        bindCellsToTextEditors();*/

        initUserColors();

        canvasTimer = new Timer();
        canvasTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCanvas();
            }
        }, 100, 1000);
    }

    private static final int MAGIC_CRUTCH_NUMBER = 500;

    private final Map<Integer, Color> userColors = new HashMap<>();
    private final Random random = new Random();
    //private final Map<Color, Circle> tooltips = new HashMap<>();

    private static class Point {
        int userId;
        double x, y;


        Point(int userId, double x, double y) {
            this.userId = userId;
            this.x = x;
            this.y = y;
        }
    }

    private void updateCanvas() {
        GraphicsContext graphicsContext = canvasField.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvasField.getWidth(), canvasField.getHeight());

        DoubleProperty alpha  = new SimpleDoubleProperty(1.0);

        double maxAlpha = 1.0;

        List<Point> points = new ArrayList<>();
        products.forEach(product -> points.add(new Point(product.getUserId(), product.getCoordinates().getX(), product.getCoordinates().getY())));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(alpha, 0)
                ),
                new KeyFrame(Duration.seconds(0.5),
                        new KeyValue(alpha, maxAlpha)
                )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(1);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                points.forEach(point -> {
                    GraphicsContext graphicsContext = canvasField.getGraphicsContext2D();

                    Color color = userColors.get(point.userId);
                    Color withAlpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha.doubleValue());
                    Circle circle = new Circle(point.x + MAGIC_CRUTCH_NUMBER,
                            point.y + MAGIC_CRUTCH_NUMBER,
                            20.0);

                    graphicsContext.setFill(withAlpha);
                    graphicsContext.fillOval(circle.getCenterX() - 20.0,
                            circle.getCenterY() - 20.0,
                            20.0 * 2,
                            20.02 * 2);

                    if (alpha.doubleValue() == maxAlpha) {
                        stop();
                    }
                });
            }
        };

        timer.start();
        timeline.play();
    }

    private void initUserColors() {
        List<User> users = new ArrayList<>();
        try {
            users = serverUserDAO.getAllUser();
        } catch (ServerAdapterException e) {
            handleServerAdapterException(e);
        }

        if (!users.isEmpty()) {
            users.forEach(concreteUser -> userColors.put(concreteUser.getId(), generateRandomColor()));
        }
    }

    private Color generateRandomColor() {
        return new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
    }

    private void handleServerAdapterException(ServerAdapterException serverAdapterException) {
        if (serverAdapterException instanceof ServerInternalErrorException) {
            showInternalErrorAlert(Localizer.getStringFromBundle("serverAnswerInternalError", "MainScreen"));
            System.exit(1);
        }

        if (serverAdapterException instanceof ServerUnavailableException) {
            showDisconnectAlert();
        }

        if (serverAdapterException instanceof AccessTokenExpiredException) {
            showAccessTokenExpiredAlert();
        }

        if (serverAdapterException instanceof WrongSignatureOfAccessTokenException) {
            showAccessTokenExpiredAlert();
        }

        if (serverAdapterException instanceof WrongQueryException) {
            showInternalErrorAlert(Localizer.getStringFromBundle("serverAnswerBadRequest", "MainScreen"));
            System.exit(1);
        }
    }

    private void showInternalErrorAlert(String string) {
        if (alert == null) {
            alert = new Alert(Alert.AlertType.ERROR, string);
            alert.showAndWait();
            alert = null;
        }
    }

    private void showWarningAlert(String errorText) {
        if (alert == null) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Storage");
            alert.setContentText(errorText);
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
                Localizer.getStringFromBundle("disconnectFormServer", "MainScreen"),
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

    private void showAccessTokenExpiredAlert() {
        if (alert == null) {
            alert = new Alert(Alert.AlertType.WARNING, Localizer.getStringFromBundle("sessionIsExpired", "MainScreen"), ButtonType.OK);
            Optional<ButtonType> response = alert.showAndWait();
            response.ifPresent(buttonType -> {
                if (buttonType.equals(ButtonType.OK)) {
                    alert = null;
                    screenContext.remove("accessToken");
                    onStop();
                    LOG_MANAGER.info("All support threads has been stop");
                    screenContext.getRouter().go("signIn");
                }
            });

            alert = null;
        }
    }

    private void reconnectToServer() {
        try {
            if (serverStudyGroupDAO.checkConnection()) {
                alert = new Alert(Alert.AlertType.INFORMATION, Localizer.getStringFromBundle("successfullyReconnected", "MainScreen"));
                alert.showAndWait();
                alert = null;
            }
        } catch (ServerAdapterException e) {
            handleServerAdapterException(e);
        }
    }

    private void initTableProperties() {
        //table.setPlaceholder(new Label(Localizer.getStringFromBundle("noProducts", "TableScreen")));
        table.setEditable(true);

        initChoiceBox("");
    }

    /*private void bindColumnsToProductFields() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        creatDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        xCoorCol.setCellValueFactory(new PropertyValueFactory<>("coordinatesX"));
        yCoorCol.setCellValueFactory(new PropertyValueFactory<>("coordinatesY"));
        studCountCol.setCellValueFactory(new PropertyValueFactory<>("studentsCount"));
        shouldBeExpCol.setCellValueFactory(new PropertyValueFactory<>("shouldBeExpelled"));
        formOfEducCol.setCellValueFactory(new PropertyValueFactory<>("formOfEducation"));
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semesterEnum"));
        n.setCellValueFactory(new PropertyValueFactory<>("organizationOrganizationName"));
        orgAnnualTurnoverColumn.setCellValueFactory(new PropertyValueFactory<>("organizationAnnualTurnover"));
        orgIdColumn.setCellValueFactory(new PropertyValueFactory<>("organizationId"));
        orgTypeColumn.setCellValueFactory(new PropertyValueFactory<>("organizationType"));
        zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("organizationAddressZipCode"));
        xLocationColumn.setCellValueFactory(new PropertyValueFactory<>("organizationAddressLocationX"));
        yLocationColumn.setCellValueFactory(new PropertyValueFactory<>("organizationAddressLocationY"));
        zLocationColumn.setCellValueFactory(new PropertyValueFactory<>("organizationAddressLocationZ"));
    }*/

    private void initChoiceBox(String s) {
        ObservableList<String> choices = FXCollections.observableArrayList(
                ID,
                USER_ID,
                NAME,
                CREATION_DATE
        );

        choice.setItems(choices);
        choice.getSelectionModel().select(0);
    }

    private void refreshFilterText() {
        String oldFilterText = filter.getText();
        filter.textProperty().setValue(" ");
        filter.textProperty().setValue(oldFilterText);
    }

    public void setStudyGroupCollectionUpdater(StudyGroupCollectionUpdater studyGroupCollectionUpdater) {
        this.studyGroupCollectionUpdater = studyGroupCollectionUpdater;
    }

    @Override
    public void change(List<StudyGroup> products) {

    }

    @Override
    public void disconnect() {

    }

    public void onStop() {
        if (studyGroupCollectionUpdater != null) {
            studyGroupCollectionUpdater.stop();
        }

        if (canvasTimer != null) {
            canvasTimer.cancel();
        }
    }
}
