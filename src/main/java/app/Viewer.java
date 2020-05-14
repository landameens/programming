package app;

import app.query.CommandName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static app.query.CommandName.*;

/**
 * This class is responsible for displaying invitation messages before user's input or command results in console.
 */
public final class Viewer {
    private final Map<String, String> addCommandMessages = new LinkedHashMap<String, String>() {
        {
            put("StudyGroupName","Введите название группы: " + System.lineSeparator());
            put("xCoordinate","Введите координату X: " + System.lineSeparator());
            put("yCoordinate","Введите координату Y: " + System.lineSeparator());
            put("studentsCount","Введите количество студентов в группе: " + System.lineSeparator());
            put("shouldBeExpelled","Введите количество студентов, которых скоро отчислят: " + System.lineSeparator());
            put("formOfEducation","Введите форму образования: {DISTANCE_EDUCATION; FULL_TIME_EDUCATION; EVENING_CLASSES;} " + System.lineSeparator());
            put("semesterEnum","Введите номер семестра: {FIRST; SECOND; FOURTH; EIGHTH;} " + System.lineSeparator());
            put("groupAdminName","Введите имя администратора группы, если Вы не хотите добавлять админа, введите null: " + System.lineSeparator());
            put("groupAdminHeight","Введите рост администратора группы: " + System.lineSeparator());
            put("groupAdminPassportID","Введите паспортные данные администратора группы: " + System.lineSeparator());
            put("groupAdminNationality","Введите национальность администратора группы: {UNITED_KINGDOM; GERMANY; VATICAN; SOUTH_KOREA; JAPAN;} " + System.lineSeparator());
        }
    };

    private final Map<String, String> countByGroupAdminCommandMessages = new LinkedHashMap<String, String>() {
        {
            put("groupAdminName","Введите имя администратора группы: ");
            put("groupAdminHeight","Введите рост администратора группы: ");
            put("groupAdminPassportID","Введите паспортные данные администратора группы: ");
            put("groupAdminNationality","Введите национальность администратора группы: {UNITED_KINGDOM; GERMANY; VATICAN; SOUTH_KOREA; JAPAN;} ");
        }
    };

    private final Map<CommandName, Map<String, String>> inputMessagesMap = new HashMap<CommandName, Map<String, String>>() {
        {
            put(ADD, addCommandMessages);
            put(ADD_IF_MIN, addCommandMessages);
            put(REMOVE_LOWER, addCommandMessages);
            put(COUNT_BY_GROUP_ADMIN, countByGroupAdminCommandMessages);
            put(UPDATE, addCommandMessages);

        }
    };

    public Map<CommandName, Map<String, String>> getInputMessagesMap() {
        return inputMessagesMap;
    }

    public String showInvitationCommandMessage(){
        return "Пожалуйста, введите команду:";
    }

    public String showEnteredNullMessage() {
        return "Вы ввели null вместо команды, повторите ввод.";
    }
}
