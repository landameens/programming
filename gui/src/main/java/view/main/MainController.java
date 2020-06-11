package view.main;

import controller.AbstractController;
import controller.localizer.Localizer;
import controller.serverAdapter.exception.ServerAdapterException;
import controller.serverAdapter.exception.ServerInternalErrorException;
import controller.serverAdapter.exception.ServerUnavailableException;
import controller.serverAdapter.exception.WrongQueryException;
import domain.exception.VerifyException;
import domain.studyGroup.FormOfEducation;
import domain.studyGroup.Semester;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.coordinates.Coordinates;
import domain.studyGroup.dao.ServerStudyGroupDAO;
import domain.studyGroup.person.Country;
import domain.studyGroup.person.Person;
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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import manager.LogManager;
import view.fxController.FXController;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.sqrt;

public class MainController extends FXController implements StudyGroupRepositorySubscriber {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(MainController.class);

    private String USER_ID = "User ID";
    private String ID = "ID";
    private String CREATION_DATE = "Creation date";
    private String STUD_COUNT = "Students count";
    private String SHOULD_BE_EXPELLED = "Should be expelled";
    private String FORM_OF_EDUCATION = "Form of education";
    private String SEMESTER = "Semester";
    private String HEIGHT = "Height";
    private String PASSPORT_ID = "Passport ID";
    private String NATIONALITY = "Nationality";

    private String X_COORDINATES = "Coordinates X";
    private String Y_COORDINATES = "Coordinates Y";
    private String STUDY_GROUP_NAME = "Study group name";
    private String PERSON_NAME = "Person name";
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
    public TableColumn<StudyGroup, String> personNameCol;
    @FXML
    public TableColumn<StudyGroup, Integer> userIdCol;
    @FXML
    public TableColumn<StudyGroup, String> nameCol;
    @FXML
    public TableColumn<StudyGroup, Integer> coordinatesCol;
    @FXML
    public TableColumn<StudyGroup, Integer> xCoorCol;
    @FXML
    public TableColumn<StudyGroup, Integer> yCoorCol;
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
    public TableColumn<StudyGroup, String> personCol;
    @FXML
    public TableColumn<StudyGroup, Country> natCol;
    @FXML
    public TableView<StudyGroup> table;
    @FXML
    public Canvas canvasField;

    private volatile ObservableList<StudyGroup> studyGroups;
    private ServerUserDAO serverUserDAO;
    private ServerStudyGroupDAO serverStudyGroupDAO;
    private User user;
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
        synchronized (LOG_MANAGER) {
            studyGroups = FXCollections.observableArrayList();
        }
        initTableProperties();
        bindColumnsToStudyGroupFields();
        initContextMenu();
        initMenuBar();

        canvasField.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            double sceneX = event.getX() - MAGIC_CRUTCH_NUMBER;
            double sceneY = event.getY() - MAGIC_CRUTCH_NUMBER;

            for (StudyGroup studyGroup : studyGroups) {
                double centerX = studyGroup.getCoordinatesX();
                double centerY = studyGroup.getCoordinatesY();

                double length = sqrt((sceneX - centerX) * (sceneX - centerX) + (sceneY - centerY) * (sceneY - centerY));

                if (length <= 20.0) {
                    table.getSelectionModel().select(studyGroup);
                }
            }
        });

        Localizer.bindTextFieldToLocale(filter, "MainScreen", "enterValue");

        Localizer.bindComponentToLocale(personCol, "MainScreen", "person");
        Localizer.bindComponentToLocale(studyGroup, "MainScreen", "studyGroup");
        Localizer.bindComponentToLocale(nameCol, "MainScreen", "name");
        Localizer.bindComponentToLocale(creatDateCol, "MainScreen", "creationDate");
        Localizer.bindComponentToLocale(coordinatesCol, "MainScreen", "coordinates");
        Localizer.bindComponentToLocale(studCountCol, "MainScreen", "studCount");
        Localizer.bindComponentToLocale(personNameCol, "MainScreen", "name");
        Localizer.bindComponentToLocale(shouldBeExpCol, "MainScreen", "shouldBeExpelled");
        Localizer.bindComponentToLocale(formOfEducCol, "MainScreen", "formOfEduc");
        Localizer.bindComponentToLocale(semesterCol, "MainScreen", "semester");
        Localizer.bindComponentToLocale(heightCol, "MainScreen", "height");
        Localizer.bindComponentToLocale(passportIdCol, "MainScreen", "passportId");
        Localizer.bindComponentToLocale(natCol, "MainScreen", "nationality");
    }

    private <T> StudyGroup getStudyGroup(TableColumn.CellEditEvent<StudyGroup, T> event) {
        TablePosition<StudyGroup, T> position = event.getTablePosition();
        int row = position.getRow();
        return event.getTableView().getItems().get(row);
    }

    private Timer canvasTimer;
    @Override
    public void onStart() {
        sceneAdapter.getStage().setFullScreen(true);
        initStudyGroupCollection();


        bindCellsToTextEditors();

        try {
            user = serverUserDAO.get(screenContext.get("login"));
        } catch (ServerAdapterException e) {
            handleServerAdapterException(e);
        }

        studyGroupCollectionUpdater.start();
        initUserColors();
        canvasTimer = new Timer();
        canvasTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateCanvas();
            }
        }, 100, 1000);
    }

    private void initMenuBar() {
        MenuItem profile = new MenuItem(Localizer.getStringFromBundle("profile", "MainScreen"));
        Localizer.bindComponentToLocale(profile, "MainScreen", "profile");
        profile.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCodeCombination.CONTROL_DOWN));
        profile.setOnAction(event -> {
            Stage newWindow = new Stage();

            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getClassLoader().getResource("markup/profile.fxml");
            loader.setLocation(url);

            ProfileController profileController = new ProfileController(user);
            loader.setController(profileController);

            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                showInternalErrorAlert(Localizer.getStringFromBundle("errorDuling", "MainScreen"));
            }

            newWindow.setScene(new Scene(parent));
            newWindow.show();
        });

        MenuItem settings = new MenuItem(Localizer.getStringFromBundle("settings", "MainScreen"));
        Localizer.bindComponentToLocale(settings, "MainScreen", "settings");
        settings.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCodeCombination.CONTROL_DOWN));
        settings.setOnAction(event -> {
            Stage newWindow = new Stage();

            FXMLLoader loader = new FXMLLoader();
            URL url = getClass().getClassLoader().getResource("markup/settings.fxml");
            loader.setLocation(url);

            SettingsController settingsController = new SettingsController(this);
            loader.setController(settingsController);

            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                showInternalErrorAlert(Localizer.getStringFromBundle("errorDuling", "MainScreen"));
            }

            newWindow.setScene(new Scene(parent));
            newWindow.setTitle("Profile");
            newWindow.show();
        });

        MenuItem logout = new MenuItem(Localizer.getStringFromBundle("logout", "MainScreen"));
        Localizer.bindComponentToLocale(logout, "MainScreen", "logout");
        logout.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCodeCombination.CONTROL_DOWN));
        logout.setOnAction(event -> {
            screenContext.remove("login");
            screenContext.remove("password");
            onStop();
            screenContext.getRouter().go("signIn");
        });

        MenuItem refreshCollection = new MenuItem(Localizer.getStringFromBundle("refresh", "MainScreen"));
        Localizer.bindComponentToLocale(refreshCollection, "MainScreen", "refresh");
        refreshCollection.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCodeCombination.CONTROL_DOWN));
        refreshCollection.setOnAction(event -> {
            try {
                change(serverStudyGroupDAO.get());
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();

        MenuItem exit = new MenuItem(Localizer.getStringFromBundle("exit", "MainScreen"));
        Localizer.bindComponentToLocale(exit, "MainScreen", "exit");
        exit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCodeCombination.CONTROL_DOWN));
        exit.setOnAction(event -> {
            System.exit(0);
        });

        menuBar.getMenus().forEach(menu -> menu.getItems().addAll(profile, settings, separatorMenuItem, refreshCollection, separatorMenuItem1, logout, exit));
    }

    public void restoreChoiceBox() {
        initChoiceBox();
    }

    private void bindCellsToTextEditors() {
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, String> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            try {
                getStudyGroup(event).setName(event.getNewValue());
            } catch (VerifyException e) {
                table.refresh();
                showErrorAlert(Localizer.getStringFromBundle("noteStudyGroup", "MainScreen"));
                return;
            }

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        xCoorCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        xCoorCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Integer> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            try {
                getStudyGroup(event).getCoordinates().setX(event.getNewValue());
            } catch (VerifyException e) {
                showWarningAlert(Localizer.getStringFromBundle("tooHighX", "MainScreen"));
                table.refresh();
                return;
            }

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        yCoorCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        yCoorCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Integer> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            getStudyGroup(event).getCoordinates().setY(event.getNewValue());

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        studCountCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        studCountCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Integer> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            try {
                getStudyGroup(event).setStudentsCount(event.getNewValue());
            } catch (VerifyException e) {
                showWarningAlert("Поле не может быть меньше нуля!");
                table.refresh();
                return;
            }

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        shouldBeExpCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        shouldBeExpCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Long> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            try {
                getStudyGroup(event).setShouldBeExpelled(event.getNewValue());
            } catch (VerifyException e) {
                showWarningAlert("Поле не может быть меньше нуля или пустым!");
                table.refresh();
                return;
            }

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        formOfEducCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(FormOfEducation.values())));
        formOfEducCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, FormOfEducation> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            getStudyGroup(event).setFormOfEducation(event.getNewValue());

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        semesterCol
                .setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(Semester.values())));
        semesterCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Semester> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            getStudyGroup(event).setSemesterEnum(event.getNewValue());

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        personNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        personNameCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, String> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            try {
                getStudyGroup(event).getGroupAdmin().setName(event.getNewValue());
            } catch (VerifyException e) {
                showWarningAlert("Поле не может быть пустым!");
                table.refresh();

                return;
            }

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        heightCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        heightCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Integer> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            try {
                getStudyGroup(event).getGroupAdmin().setHeight(event.getNewValue());
            } catch (VerifyException e) {
                showWarningAlert("Неверный формат! Введите число");
                table.refresh();
                return;
            }

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        passportIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passportIdCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, String> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            try {
                getStudyGroup(event).getGroupAdmin().setPassportID(event.getNewValue());
            } catch (VerifyException e) {
                showWarningAlert("Поле не может быть пустым!");
                table.refresh();
                return;
            }

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        natCol
                .setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(Country.values())));
        natCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Country> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            getStudyGroup(event).getGroupAdmin().setNationality(event.getNewValue());

            change(studyGroups);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });
    }

    private void initContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteStudyGroup = new MenuItem("delete");
        deleteStudyGroup.setOnAction(event -> {
            StudyGroup selectedItem = table.getSelectionModel().getSelectedItem();
            List<StudyGroup> localList = new ArrayList<>(studyGroups);

            if (selectedItem == null) {
                showErrorAlert(Localizer.getStringFromBundle("noteDelete", "MainScreen"));
                table.refresh();
                return;
            }

            if (selectedItem.getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "MainScreen"));
                table.refresh();
                return;
            }

            localList.remove(selectedItem);

            synchronized (LOG_MANAGER) {
                studyGroups = FXCollections.observableArrayList(localList);
            }
            table.setItems(studyGroups);

            try {
                serverStudyGroupDAO.delete(selectedItem.getId());
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }

            refreshFilterText();
        });

        MenuItem addGroup = new MenuItem("add");
        addGroup.setOnAction(event -> {
            Person person;
            try {
                person = new Person("1", 1, "1", Country.JAPAN);
            } catch (VerifyException e) {
                LOG_MANAGER.errorThrowable(e);
                throw new RuntimeException(e);
            }

            StudyGroup studyGroup;
            try {
                studyGroup = new StudyGroup(1L,
                                                    1,
                                                    "a",
                                                    new Coordinates(1,1 ),
                                                    LocalDateTime.now(),
                                                    1,
                                                    1L,
                                                    FormOfEducation.DISTANCE_EDUCATION,
                                                    Semester.EIGHTH,
                                                    person);
            } catch (VerifyException e) {
                LOG_MANAGER.errorThrowable(e);
                throw new RuntimeException(e);
            }

            List<StudyGroup> localList = new ArrayList<>(studyGroups);

            localList.add(studyGroup);

            synchronized (LOG_MANAGER) {
                studyGroups = FXCollections.observableArrayList(localList);
            }

            table.setItems(studyGroups);

            try {
                serverStudyGroupDAO.create(studyGroup);
                change(serverStudyGroupDAO.get());
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }

            refreshFilterText();
        });

        contextMenu.getItems().add(deleteStudyGroup);
        contextMenu.getItems().add(addGroup);

        table.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(table, event.getScreenX(), event.getScreenY());
            }
        });
    }

    private void initFilter(FilteredList<StudyGroup> filteredList) {
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(studyGroup -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                String filterProperty = choice.getValue();

                if (ID.equals(filterProperty)) {
                    return Long.toString(studyGroup.getId()).toLowerCase().contains(lowerCaseFilter);
                } else if (USER_ID.equals(filterProperty)) {
                    return Integer.toString(studyGroup.getUserId()).toLowerCase().contains(lowerCaseFilter);
                } else if (STUDY_GROUP_NAME.equals(filterProperty)) {
                    return studyGroup.getName().toLowerCase().contains(lowerCaseFilter);
                } else if (CREATION_DATE.equals(filterProperty)) {
                    return studyGroup.getCreationDate().toLowerCase().contains(lowerCaseFilter);
                } else if (X_COORDINATES.equals(filterProperty)) {
                    return Integer.toString(studyGroup.getCoordinatesX()).toLowerCase().contains(lowerCaseFilter);
                } else if (Y_COORDINATES.equals(filterProperty)) {
                    return Double.toString(studyGroup.getCoordinatesY()).toLowerCase().contains(lowerCaseFilter);
                } else if (STUD_COUNT.equals(filterProperty)) {
                    return Integer.toString(studyGroup.getStudentsCount()).toLowerCase().contains(lowerCaseFilter);
                } else if (SHOULD_BE_EXPELLED.equals(filterProperty)) {
                    return Long.toString(studyGroup.getShouldBeExpelled()).toLowerCase().contains(lowerCaseFilter);
                } else if (FORM_OF_EDUCATION.equals(filterProperty)) {
                    return studyGroup.getFormOfEducation().getName().toLowerCase().contains(lowerCaseFilter);
                } else if (SEMESTER.equals(filterProperty)) {
                    return studyGroup.getSemesterEnum().getName().toLowerCase().contains(lowerCaseFilter);
                } else if (PERSON_NAME.equals(filterProperty)) {
                    return studyGroup.getPersonName().toLowerCase().contains(lowerCaseFilter);
                } else if (HEIGHT.equals(filterProperty)) {
                    return Integer.toString(studyGroup.getPersonHeight()).toLowerCase().contains(lowerCaseFilter);
                } else if (PASSPORT_ID.equals(filterProperty)) {
                    return studyGroup.getPersonPassportID().toLowerCase().contains(lowerCaseFilter);
                } else if (NATIONALITY.equals(filterProperty)) {
                    return studyGroup.getPersonNationality().getName().toLowerCase().contains(lowerCaseFilter);
                }
                return false;
            });
        });
    }

    private void initStudyGroupCollection() {
        ObservableList<StudyGroup> studyGroups = null;
        try {
            studyGroups = FXCollections.observableArrayList(serverStudyGroupDAO.get());
        } catch (ServerAdapterException e) {
            handleServerAdapterException(e);
        }

        FilteredList<StudyGroup> filteredList = new FilteredList<>(studyGroups, studyGroup -> true);
        initFilter(filteredList);

        SortedList<StudyGroup> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());

        synchronized (LOG_MANAGER) {
            this.studyGroups = sortedList;
        }

        table.setItems(this.studyGroups);
    }

    private static final int MAGIC_CRUTCH_NUMBER = 500;

    private final Map<Integer, Color> userColors = new HashMap<>();
    private final Random random = new Random();

    private static class Point {
        int userId;
        double x, y;


        Point(int userId, double x, double y) {
            this.userId = userId;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "userId=" + userId +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private void updateCanvas() {
        GraphicsContext graphicsContext = canvasField.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvasField.getWidth(), canvasField.getHeight());

        DoubleProperty alpha  = new SimpleDoubleProperty(1.0);

        double maxAlpha = 1.0;

        List<Point> points = new ArrayList<>();
        studyGroups.forEach(studyGroup -> points.add(new Point(studyGroup.getUserId(), studyGroup.getCoordinates().getX(), studyGroup.getCoordinates().getY())));

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
                            20.0 * 2);

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
            alert.setTitle("Nadya lab");
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
        // todo wew
        //table.setPlaceholder(new Label(Localizer.getStringFromBundle("noProducts", "TableScreen")));
        table.setEditable(true);

        initChoiceBox();
    }

    private void bindColumnsToStudyGroupFields() {
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
        personNameCol.setCellValueFactory(new PropertyValueFactory<>("personName"));
        heightCol.setCellValueFactory(new PropertyValueFactory<>("personHeight"));
        passportIdCol.setCellValueFactory(new PropertyValueFactory<>("personPassportID"));
        natCol.setCellValueFactory(new PropertyValueFactory<>("personNationality"));
    }

    private void initChoiceBox() {
        STUDY_GROUP_NAME = Localizer.getStringFromBundle("studyGroupName", "MainScreen");
        CREATION_DATE = Localizer.getStringFromBundle("creationDate", "MainScreen");
        STUD_COUNT = Localizer.getStringFromBundle("studCount", "MainScreen");
        SHOULD_BE_EXPELLED = Localizer.getStringFromBundle("shouldBeExpelled", "MainScreen");
        FORM_OF_EDUCATION = Localizer.getStringFromBundle("formOfEduc", "MainScreen");
        SEMESTER = Localizer.getStringFromBundle("semester", "MainScreen");
        PERSON_NAME = Localizer.getStringFromBundle("personName", "MainScreen");
        HEIGHT = Localizer.getStringFromBundle("height", "MainScreen");
        PASSPORT_ID = Localizer.getStringFromBundle("passportId", "MainScreen");
        NATIONALITY = Localizer.getStringFromBundle("nationality", "MainScreen");
        X_COORDINATES = Localizer.getStringFromBundle("xCoordinates", "MainScreen");
        Y_COORDINATES = Localizer.getStringFromBundle("yCoordinates", "MainScreen");
        ObservableList<String> choices = FXCollections.observableArrayList(
                ID,
                USER_ID,
                STUDY_GROUP_NAME,
                CREATION_DATE,
                X_COORDINATES,
                Y_COORDINATES,
                STUD_COUNT,
                SHOULD_BE_EXPELLED,
                FORM_OF_EDUCATION,
                SEMESTER,
                PERSON_NAME,
                HEIGHT,
                PASSPORT_ID,
                NATIONALITY
        );

        choice.setItems(choices);
        choice.getSelectionModel().select(0);
    }

    private void refreshFilterText() {
//        String oldFilterText = filter.getText();
//        filter.textProperty().setValue(" ");
//        filter.textProperty().setValue(oldFilterText);
    }

    public void setStudyGroupCollectionUpdater(StudyGroupCollectionUpdater studyGroupCollectionUpdater) {
        this.studyGroupCollectionUpdater = studyGroupCollectionUpdater;
    }

    @Override
    public void change(List<StudyGroup> studyGroups) {
        ObservableList<StudyGroup> rawStudyGroups = FXCollections.observableArrayList(studyGroups);
        FilteredList<StudyGroup> filteredList = new FilteredList<>(rawStudyGroups, group -> true);

        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(studyGroup -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                String filterProperty = choice.getValue();

                if (ID.equals(filterProperty)) {
                    return Long.toString(studyGroup.getId()).toLowerCase().contains(lowerCaseFilter);
                } else if (USER_ID.equals(filterProperty)) {
                    return Integer.toString(studyGroup.getUserId()).contains(lowerCaseFilter);
                } else if (STUDY_GROUP_NAME.equals(filterProperty)) {
                    return studyGroup.getName().toLowerCase().contains(lowerCaseFilter);
                } else if (CREATION_DATE.equals(filterProperty)) {
                    return studyGroup.getCreationDate().toLowerCase().contains(lowerCaseFilter);
                } else if (X_COORDINATES.equals(filterProperty)) {
                    return Integer.toString(studyGroup.getCoordinatesX()).toLowerCase().contains(lowerCaseFilter);
                } else if (Y_COORDINATES.equals(filterProperty)) {
                    return Double.toString(studyGroup.getCoordinatesY()).toLowerCase().contains(lowerCaseFilter);
                } else if (STUD_COUNT.equals(filterProperty)) {
                    return Integer.toString(studyGroup.getStudentsCount()).toLowerCase().contains(lowerCaseFilter);
                } else if (SHOULD_BE_EXPELLED.equals(filterProperty)) {
                    return Long.toString(studyGroup.getShouldBeExpelled()).toLowerCase().contains(lowerCaseFilter);
                } else if (FORM_OF_EDUCATION.equals(filterProperty)) {
                    return studyGroup.getFormOfEducation().getName().toLowerCase().contains(lowerCaseFilter);
                } else if (SEMESTER.equals(filterProperty)) {
                    return studyGroup.getSemesterEnum().getName().toLowerCase().contains(lowerCaseFilter);
                } else if (PERSON_NAME.equals(filterProperty)) {
                    return studyGroup.getPersonName().toLowerCase().contains(lowerCaseFilter);
                } else if (HEIGHT.equals(filterProperty)) {
                    return Integer.toString(studyGroup.getPersonHeight()).toLowerCase().contains(lowerCaseFilter);
                } else if (PASSPORT_ID.equals(filterProperty)) {
                    return studyGroup.getPersonPassportID().toLowerCase().contains(lowerCaseFilter);
                } else if (NATIONALITY.equals(filterProperty)) {
                    return studyGroup.getPersonNationality().getName().toLowerCase().contains(lowerCaseFilter);
                }
                return false;
            });
        });

        SortedList<StudyGroup> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        synchronized (LOG_MANAGER) {
            this.studyGroups = sortedList;
        }

        table.setItems(this.studyGroups);

        Platform.runLater(this::refreshFilterText);

        updateCanvas();
    }

    @Override
    public void disconnect() {
        Platform.runLater(this::showDisconnectAlert);
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
