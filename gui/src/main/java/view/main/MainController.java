package view.main;

import controller.AbstractController;
import controller.localizer.Localizer;
import controller.serverAdapter.exception.ServerAdapterException;
import domain.exception.VerifyException;
import domain.studyGroup.FormOfEducation;
import domain.studyGroup.Semester;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.dao.ServerStudyGroupDAO;
import domain.studyGroup.person.Country;
import domain.studyGroup.person.Person;
import domain.studyGroupRepository.ProductCollectionUpdater;
import domain.studyGroupRepository.StudyGroupRepositorySubscriber;
import domain.user.ServerUserDAO;
import domain.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;
import view.fxController.FXController;

import java.util.ArrayList;
import java.util.List;

public class MainController extends FXController implements StudyGroupRepositorySubscriber {
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

    private static final String X_COORDINATES = "Coordinates X";
    private static final String Y_COORDINATES = "Coordinates Y";
    private static final String STUDY_GROUP_NAME = "Study group name";
    private static final String PERSON_NAME = "Person name";
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
    public TableColumn<StudyGroup, Country> natCol;
    @FXML
    public TableView<StudyGroup> table;

    private ObservableList<StudyGroup> products;
    private ServerUserDAO serverUserDAO;
    private ServerStudyGroupDAO serverStudyGroupDAO;
    private ProductCollectionUpdater productCollectionUpdater;
    private User user;


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
        bindColumnsToProductFields();
        initContextMenu();

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

    private <T> StudyGroup getStudyGroup(TableColumn.CellEditEvent<StudyGroup, T> event) {
        TablePosition<StudyGroup, T> position = event.getTablePosition();
        int row = position.getRow();
        return event.getTableView().getItems().get(row);
    }

    @Override
    public void onStart() {
        sceneAdapter.getStage().setFullScreen(true);

        initStudyGroupCollection();

        bindCellsToTextEditors();
    }

    private void bindCellsToTextEditors() {
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, String> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            try {
                getStudyGroup(event).setName(event.getNewValue());
            } catch (VerifyException e) {
                table.refresh();
                showErrorAlert(Localizer.getStringFromBundle("noteStudyGroup", "TableScreen"));
            }

            change(products);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        xCoorCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        xCoorCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Integer> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).getCoordinates().setX(event.getNewValue());

            change(products);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        yCoorCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        yCoorCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Integer> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).getCoordinates().setY(event.getNewValue());

            change(products);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        studCountCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        studCountCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Integer> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).setStudentsCount(event.getNewValue());

            change(products);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        shouldBeExpCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        shouldBeExpCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Long> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).setShouldBeExpelled(event.getNewValue());

            change(products);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        formOfEducCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(FormOfEducation.values())));
        formOfEducCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, FormOfEducation> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).setFormOfEducation(event.getNewValue());

            change(products);

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
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).setSemesterEnum(event.getNewValue());

            change(products);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        personNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        personNameCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, String> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).getGroupAdmin().setName(event.getNewValue());

            change(products);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        heightCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        heightCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, Integer> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).getGroupAdmin().setHeight(event.getNewValue());

            change(products);

            try {
                serverStudyGroupDAO.update(getStudyGroup(event));
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }
        });

        passportIdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passportIdCol.setOnEditCommit((TableColumn.CellEditEvent<StudyGroup, String> event) -> {
            if (getStudyGroup(event).getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).getGroupAdmin().setPassportID(event.getNewValue());

            change(products);

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
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            getStudyGroup(event).getGroupAdmin().setNationality(event.getNewValue());

            change(products);

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
            List<StudyGroup> localList = new ArrayList<>(products);

            if (selectedItem == null) {
                showErrorAlert(Localizer.getStringFromBundle("noteDelete", "TableScreen"));
                return;
            }

            if (selectedItem.getUserId() != user.getId()) {
                showWarningAlert(Localizer.getStringFromBundle("notYoursDelete", "TableScreen"));
                return;
            }

            localList.remove(selectedItem);

            products = FXCollections.observableArrayList(localList);
            table.setItems(products);

            try {
                serverStudyGroupDAO.delete(selectedItem.getId());
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }

            refreshFilterText();
        });

        MenuItem addGroup = new MenuItem("add");
        addGroup.setOnAction(event -> {
            Person person = new Person();
            StudyGroup studyGroup = new StudyGroup();
            studyGroup.setGroupAdmin(person);

            List<StudyGroup> localList = new ArrayList<>(products);

            localList.add(studyGroup);

            products = FXCollections.observableArrayList(localList);
            table.setItems(products);

            try {
                serverStudyGroupDAO.create(studyGroup);
            } catch (ServerAdapterException e) {
                handleServerAdapterException(e);
            }

            refreshFilterText();
        });

        contextMenu.getItems().add(deleteStudyGroup);

        table.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(table, event.getScreenX(), event.getScreenY());
            }
        });
    }

    private void initFilter(FilteredList<StudyGroup> filteredList) {
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(studyGroup -> {
                if (newValue == null || newValue.isEmpty()) {
                    return false;
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
                    return studyGroup.getPersonPassportId().toLowerCase().contains(lowerCaseFilter);
                } else if (NATIONALITY.equals(filterProperty)) {
                    return studyGroup.getPersonNationality().getName().toLowerCase().contains(lowerCaseFilter);
                }
                return false;
            });
        });
    }

    private void initStudyGroupCollection() {
        ObservableList<StudyGroup> rawProducts = null;
        try {
            rawProducts = FXCollections.observableArrayList(serverStudyGroupDAO.get());
        } catch (ServerAdapterException e) {
            // todo Check
            //handleServerAdapterException(e);
        }

        FilteredList<StudyGroup> filteredList = new FilteredList<>(rawProducts, product -> true);
        initFilter(filteredList);

        SortedList<StudyGroup> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        products = sortedList;

        table.setItems(products);
    }

    private void initTableProperties() {
        // todo wew
        //table.setPlaceholder(new Label(Localizer.getStringFromBundle("noProducts", "TableScreen")));
        table.setEditable(true);

        initChoiceBox();
    }

    private void bindColumnsToProductFields() {
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
        String oldFilterText = filter.getText();
        filter.textProperty().setValue(" ");
        filter.textProperty().setValue(oldFilterText);
    }

    public void setProductCollectionUpdater(ProductCollectionUpdater productCollectionUpdater) {
        this.productCollectionUpdater = productCollectionUpdater;
    }

    @Override
    public void change(List<StudyGroup> products) {

    }

    @Override
    public void disconnect() {

    }
}
