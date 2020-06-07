package app;

import app.controller.commands.mainScreen.CommandName;
import controller.components.serviceMediator.Service;

import java.util.*;

import static app.controller.commands.mainScreen.CommandName.*;

/**
 * This class is responsible for displaying results of commands in console.
 */
public final class Viewer implements Service {
    private final String GREETINGS_LINE_1;
    private final String GREETINGS_LINE_2;
    private final String INTERNAL_CLIENT_ERROR_MESSAGE;
    private final String INTERNAL_SERVER_ERROR_MESSAGE;
    private final String CLIENT_CANNOT_CONNECT_TO_SERVER_ERROR_MESSAGE;
    private final String LOST_CONNECTION_ERROR_MESSAGE;
    private final String GET_WRONG_MESSAGE_ERROR_MESSAGE;
    private final String NOT_YET_CONNECTED_ERROR_MESSAGE;
    private final String CONNECTED_SUCCESSFULLY_MESSAGE;
    private final String BAD_REQUEST_ERROR_MESSAGE;
    private final String NULL_ENTER_INPUT;
    private final String PREFIX_SERVER_ANSWER;
    private final String UNSUCCESSFULLY_COMMAND_MESSAGE;
    private final String NO_SUCH_COMMAND_ERROR_MESSAGE;
    private final String OFFER_TO_REPEAT_INPUT;
    private final String GOOD_BYE_LINE_1;
    private final String GOOD_BYE_LINE_2;
    private final String ERROR_PREFIX;


    public Viewer() {
        ResourceBundle resourceBundle
                = ResourceBundle.getBundle("localizedStrings.Viewer", Locale.getDefault());
        GREETINGS_LINE_1 = resourceBundle.getString("standardMessages.greetings.line1");
        GREETINGS_LINE_2 = resourceBundle.getString("standardMessages.greetings.line2");
        INTERNAL_SERVER_ERROR_MESSAGE = resourceBundle.getString("exceptionMessages.internalServerError");
        INTERNAL_CLIENT_ERROR_MESSAGE = resourceBundle.getString("exceptionMessages.internalClientError");
        CLIENT_CANNOT_CONNECT_TO_SERVER_ERROR_MESSAGE =
                resourceBundle.getString("exceptionMessages.cannotConnectToServer");
        LOST_CONNECTION_ERROR_MESSAGE =
                resourceBundle.getString("exceptionMessages.lostConnection");
        GET_WRONG_MESSAGE_ERROR_MESSAGE =
                resourceBundle.getString("exceptionMessages.getWrongMessage");
        NOT_YET_CONNECTED_ERROR_MESSAGE =
                resourceBundle.getString("exceptionMessages.notYetConnected");
        BAD_REQUEST_ERROR_MESSAGE = resourceBundle.getString("exceptionMessages.badRequest");
        NO_SUCH_COMMAND_ERROR_MESSAGE = resourceBundle.getString("exceptionMessages.noSuchCommand");
        ERROR_PREFIX = resourceBundle.getString("exceptionMessages.errorPrefix");
        NULL_ENTER_INPUT = resourceBundle.getString("standardMessages.nullEnterInput");
        PREFIX_SERVER_ANSWER = resourceBundle.getString("standardMessages.prefixServerAnswer");
        UNSUCCESSFULLY_COMMAND_MESSAGE = resourceBundle.getString("exceptionMessages.unsuccessfullyCommand");
        OFFER_TO_REPEAT_INPUT = resourceBundle.getString("standardMessages.offerToRepeatInput");
        GOOD_BYE_LINE_1 = resourceBundle.getString("standardMessages.goodBye.line1");
        GOOD_BYE_LINE_2 = resourceBundle.getString("standardMessages.goodBye.line2");
        CONNECTED_SUCCESSFULLY_MESSAGE = resourceBundle.getString("standardMessages.connectedSuccessfully");
    }


    public String showGreetingMessage() {
        return GREETINGS_LINE_1 + System.lineSeparator() + GREETINGS_LINE_2 + System.lineSeparator();
    }
    
    public String showInternalServerErrorMessage() {
        return getErrorPrefix() + INTERNAL_SERVER_ERROR_MESSAGE;
    }

    public String showInternalClientErrorMessage() {
        return getErrorPrefix() + INTERNAL_CLIENT_ERROR_MESSAGE;
    }

    public String showClientCannotConnectToServerErrorMessage() {
        return getErrorPrefix() + CLIENT_CANNOT_CONNECT_TO_SERVER_ERROR_MESSAGE;
    }

    public String showClientLostConnectionErrorMessage() {
        return getErrorPrefix() + LOST_CONNECTION_ERROR_MESSAGE;
    }

    public String showGetWrongMessageErrorMessage() {
        return getErrorPrefix() + GET_WRONG_MESSAGE_ERROR_MESSAGE;
    }

    public String showNotYetConnectedErrorMessage() {
        return getErrorPrefix() + NOT_YET_CONNECTED_ERROR_MESSAGE;
    }

    public String showConnectedSuccessfullyMessage() {
        return CONNECTED_SUCCESSFULLY_MESSAGE;
    }

    public String showBadRequestErrorMessage() {
        return getErrorPrefix() + BAD_REQUEST_ERROR_MESSAGE;
    }

    public String showNullEnterInput() {
        return NULL_ENTER_INPUT;
    }

    public String showPrefixServerAnswer() {
        return PREFIX_SERVER_ANSWER;
    }

    public String showUnsuccessfulCommandMessage() {
        return UNSUCCESSFULLY_COMMAND_MESSAGE;
    }

    public String showNoSuchCommandErrorMessage() {
        return getErrorPrefix() + NO_SUCH_COMMAND_ERROR_MESSAGE;
    }
    
    public String showOfferToRepeatInput() {
        return OFFER_TO_REPEAT_INPUT;
    }
    
    public String showGoodbyeMessage() {
        return GOOD_BYE_LINE_1 + System.lineSeparator() + GOOD_BYE_LINE_2;
    }
    
    public String getErrorPrefix() {
        return ERROR_PREFIX;
    }


    private final Map<String, String> addCommandMessages = new LinkedHashMap<String, String>() {
        {
            put("StudyGroupName","Введите название группы: " + System.lineSeparator());
            put("xCoordinate","Введите координату X: " + System.lineSeparator());
            put("yCoordinate","Введите координату Y: " + System.lineSeparator());
            put("studentsCount","Введите количество студентов в группе: " + System.lineSeparator());
            put("shouldBeExpelled","Введите количество студентов, которых скоро отчислят: " + System.lineSeparator());
            put("formOfEducation","Введите форму образования: {DISTANCE_EDUCATION; FULL_TIME_EDUCATION; EVENING_CLASSES;} " + System.lineSeparator());
            put("semesterEnum","Введите номер семестра: {FIRST; SECOND; FOURTH; EIGHTH;} " + System.lineSeparator());
            put("groupAdminName","Введите имя администратора группы: " + System.lineSeparator());
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

    public String showExitingMessage() {
        return "Осуществлен выход. \nЯ буду скучать :(";
    }

    public String showNonWorkingServerException() {
        return "Невозможно создать соединение с сервером, попробуйте повторить попытку позже";
    }
}
