package view.main;

import controller.AbstractController;
import domain.studyGroup.FormOfEducation;
import domain.studyGroup.Semester;
import domain.studyGroup.StudyGroup;
import domain.studyGroup.dao.ServerStudyGroupDAO;
import domain.studyGroup.person.Country;
import domain.studyGroupRepository.ProductCollectionUpdater;
import domain.studyGroupRepository.StudyGroupRepositorySubscriber;
import domain.user.ServerUserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import view.fxController.FXController;

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

    private ObservableList<StudyGroup> products;
    private ServerUserDAO serverUserDAO;
    private ServerStudyGroupDAO serverStudyGroupDAO;
    private ProductCollectionUpdater productCollectionUpdater;


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
