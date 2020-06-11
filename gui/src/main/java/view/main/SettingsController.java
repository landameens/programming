package view.main;

import controller.localizer.Localizer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.Locale;

public class SettingsController {
    private static String ROMANIAN = Localizer.getStringFromBundle("romanian", "MenuScreen");
    private static String RUSSIAN = Localizer.getStringFromBundle("russian", "MenuScreen");
    private static String SPANISH = Localizer.getStringFromBundle("spanish", "MenuScreen");
    private static String GREEK = Localizer.getStringFromBundle("greek", "MenuScreen");

    private MainController mainController;
    @FXML
    private ComboBox<String> localeComboBox;

    @FXML
    private Label settingsLabel;

    @FXML
    private Label localeLabel;

    public SettingsController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        initLocaleComboBox();

        Localizer.bindComponentToLocale(settingsLabel, "MenuScreen", "settings");
        Localizer.bindComponentToLocale(localeLabel, "MenuScreen", "locale");

        localeComboBox.setOnAction(event -> {
            String newValue = localeComboBox.getSelectionModel().getSelectedItem();

            if (ROMANIAN.equals(newValue)) {
                Localizer.switchLanguage("ro");
            } else if (RUSSIAN.equals(newValue)) {
                Localizer.switchLanguage("ru");
            } else if (SPANISH.equals(newValue)) {
                Localizer.switchLanguage("es");
            } else if (GREEK.equals(newValue)) {
                Localizer.switchLanguage("el");
            }

            initStr();
            mainController.restoreChoiceBox();
        });
    }

    private void initStr() {
        ROMANIAN = Localizer.getStringFromBundle("romanian", "MenuScreen");
        RUSSIAN = Localizer.getStringFromBundle("russian", "MenuScreen");
        SPANISH = Localizer.getStringFromBundle("spanish", "MenuScreen");
        GREEK = Localizer.getStringFromBundle("greek", "MenuScreen");

        initLocaleComboBox();
    }

    private void initLocaleComboBox() {
        ObservableList<String> locales = FXCollections.observableArrayList(
                ROMANIAN,
                RUSSIAN,
                SPANISH,
                GREEK
        );

        localeComboBox.setItems(locales);

        Locale locale = Localizer.getLocale();


        if ("ru".equals(locale.getLanguage())) {
            localeComboBox.getSelectionModel().select(RUSSIAN);
        } else if ("ro".equals(locale.getLanguage())) {
            localeComboBox.getSelectionModel().select(ROMANIAN);
        } else if ("es".equals(locale.getLanguage())) {
            localeComboBox.getSelectionModel().select(SPANISH);
        } else if ("el".equals(locale.getLanguage())) {
            localeComboBox.getSelectionModel().select(GREEK);
        }
    }
}
