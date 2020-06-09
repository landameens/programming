package controller.localizer;

import domain.studyGroup.StudyGroup;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.util.*;

public class Localizer {
    private static final ObjectProperty<Locale> locale;

    private static final Locale NEW_ZELAND_ENGLISH = Locale.ENGLISH;
    private static final Locale SWEDISH = new Locale("sv", "SE");
    private static final Locale ICELANDIC = new Locale("is", "IS");
    private static final Locale RUSSIAN = new Locale("ru", "RU");

    private final static Map<String, Locale> LOCALE_MAP = new HashMap<String, Locale>() {
        {
            put("en", NEW_ZELAND_ENGLISH);
            put("sv", SWEDISH);
            put("is", ICELANDIC);
            put("ru", RUSSIAN);
        }
    };

    static {
        locale = new SimpleObjectProperty<>(getDefaultLocale());
        locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
    }

    public static List<Locale> getSupportedLocales() {
        return new ArrayList<>(Arrays.asList(NEW_ZELAND_ENGLISH,
                SWEDISH,
                ICELANDIC,
                RUSSIAN));
    }

    public static Locale getDefaultLocale() {
        Locale sysDefault = Locale.getDefault();
        return getSupportedLocales().contains(sysDefault) ? sysDefault : NEW_ZELAND_ENGLISH;
    }

    public static Locale getLocale() {
        return locale.get();
    }

    public static void switchLanguage(String string) {
        Locale locale = LOCALE_MAP.get(string);
        getLocaleProperty().set(locale);
        Locale.setDefault(locale);
    }

    public static ObjectProperty<Locale> getLocaleProperty() {
        return locale;
    }

    /**
     * gets the string with the given key from the resource bundle for the current locale and uses it as first argument
     * to MessageFormat.format, passing in the optional args and returning the result.
     */
    public static String getStringFromBundle(String key, String resBundleName) {
        ResourceBundle bundle = ResourceBundle.getBundle("localizedStrings/" + resBundleName, getLocale());
        return bundle.getString(key);
    }

    /**
     * creates a String binding to a localized String for the given message bundle key
     */
    public static StringBinding createStringBinding(String key, String resBundleName) {
        return Bindings.createStringBinding(() -> getStringFromBundle(key, resBundleName), locale);
    }

    public static <T extends Labeled> void bindComponentToLocale(T component,
                                                                 String resBundleName,
                                                                 String resBundleKey) {
        component.textProperty().bind(createStringBinding(resBundleKey, resBundleName));
    }

    public static <T> void bindComponentToLocale(TableColumn<StudyGroup, T> tableColumn,
                                                 String resBundleName,
                                                 String resBundleKey) {
        tableColumn.textProperty().bind(createStringBinding(resBundleKey, resBundleName));
    }

    public static void bindComponentToLocale(MenuItem menuItem,
                                             String resBundleName,
                                             String resBundleKey) {
        menuItem.textProperty().bind(createStringBinding(resBundleKey, resBundleName));
    }

    public static void bindTextFieldToLocale(TextField textField,
                                             String resBundleName,
                                             String resBundleKey) {
        textField.promptTextProperty().bind(createStringBinding(resBundleKey, resBundleName));
    }
}
