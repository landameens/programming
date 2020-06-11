package view.main;

import controller.localizer.Localizer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.Locale;

public class SettingsController {
    /*private static String ENGLISH = Localizer.getStringFromBundle("english", "SettingScreen");
    private static String RUSSIAN = Localizer.getStringFromBundle("russian", "SettingScreen");
    private static String SWEDISH = Localizer.getStringFromBundle("swedish", "SettingScreen");
    private static String ICELAND = Localizer.getStringFromBundle("icelandic", "SettingScreen");*//*

    private TableController tableController;
    @FXML
    private ComboBox<String> localeComboBox;

    @FXML
    private Label settingsLabel;

    @FXML
    private Label localeLabel;

    public SettingsController(TableController tableController) {
        this.tableController = tableController;
    }

    @FXML
    private void initialize() {
        initLocaleComboBox();

        Localizer.bindComponentToLocale(settingsLabel, "SettingScreen", "settingLabel");
        Localizer.bindComponentToLocale(localeLabel, "SettingScreen", "localeLabel");

        localeComboBox.setOnAction(event -> {
            String newValue = localeComboBox.getSelectionModel().getSelectedItem();

            System.err.println(Localizer.getLocale().getLanguage());

            if (ENGLISH.equals(newValue)) {
                Localizer.switchLanguage("en");
            } else if (RUSSIAN.equals(newValue)) {
                Localizer.switchLanguage("ru");
            } else if (SWEDISH.equals(newValue)) {
                Localizer.switchLanguage("sv");
            } else if (ICELAND.equals(newValue)) {
                Localizer.switchLanguage("is");
            }

            System.err.println(Localizer.getLocale().getLanguage());
            initStr();
            tableController.restoreAfterLocale();
        });
    }

    private void initStr() {
        ENGLISH = Localizer.getStringFromBundle("english", "SettingScreen");
        RUSSIAN = Localizer.getStringFromBundle("russian", "SettingScreen");
        SWEDISH = Localizer.getStringFromBundle("swedish", "SettingScreen");
        ICELAND = Localizer.getStringFromBundle("icelandic", "SettingScreen");

        initLocaleComboBox();
    }

    private void initLocaleComboBox() {
        ObservableList<String> locales = FXCollections.observableArrayList(
                ENGLISH,
                RUSSIAN,
                SWEDISH,
                ICELAND
        );

        localeComboBox.setItems(locales);

        Locale locale = Localizer.getLocale();


        if ("ru".equals(locale.getLanguage())) {
            localeComboBox.getSelectionModel().select(RUSSIAN);
        } else if ("en".equals(locale.getLanguage())) {
            localeComboBox.getSelectionModel().select(ENGLISH);
        } else if ("sv".equals(locale.getLanguage())) {
            localeComboBox.getSelectionModel().select(SWEDISH);
        } else if ("is".equals(locale.getLanguage())) {
            localeComboBox.getSelectionModel().select(ICELAND);
        }
    }*/
}
