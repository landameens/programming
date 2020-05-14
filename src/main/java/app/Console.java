package app;

import app.Exceptions.InputException;
import app.Exceptions.InternalException;
import app.query.CommandName;
import app.query.CommandType;
import app.query.Query;
import app.query.queryBuilder.QueryBuilder;
import app.query.queryBuilder.QueryBuilderFactory;
import controller.Controller;
import controller.response.Response;
import domain.exception.CreationException;
import manager.LogManager;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for input-output, it forms the query and handles it to controller to get response.
 */
public final class Console {
    private static final String START_MESSAGE_STRING = "Добро пожаловать в наше приложение! Для ознакомления с существующими командами, введите команду 'help'." + System.lineSeparator();
    private static final String INTERNAL_ERROR_WITH_IO = "Ошибка ввода-вывода. ";

    private final BufferedReader reader;
    private final BufferedOutputStream writer;
    private final Interpretator interpretator;
    private final Validator validator;
    private final Viewer viewer;
    private final Controller controller;
    private final QueryBuilderFactory queryBuilderFactory;

    private static final LogManager LOG_MANAGER = LogManager.createDefault(Console.class);

    public Console(InputStream input,
                   OutputStream output,
                   Controller controller) {
        this.reader = new BufferedReader(new InputStreamReader(input));
        this.writer = new BufferedOutputStream(output);

        this.controller = controller;

        this.interpretator = new Interpretator();
        this.validator = new Validator();
        this.viewer = new Viewer();
        this.queryBuilderFactory = new QueryBuilderFactory(validator, interpretator);
    }

    public void start() throws InputException {
        writeLine(START_MESSAGE_STRING);

        while (true) {
            writeLine(viewer.showInvitationCommandMessage());

            LOG_MANAGER.info("Ввод команды...");
            String command = readLine();

            if (command == null) {
                LOG_MANAGER.warn("Была введена пустая строка.");
                writeLine(viewer.showEnteredNullMessage());
                continue;
            }

            LOG_MANAGER.info("Команда введена.");
            String[] commandArray = command.split("[\\s]+");

            try {
                validator.validateCommandName(commandArray[0]);
            } catch (InputException e) {
                LOG_MANAGER.error("Произошла ошибка ввода команды...");
                writeLine(e.getMessage());
                continue;
            }

            CommandName commandName = CommandName.getCommandNameEnum(commandArray[0]);
            CommandType commandType = interpretator.interpretateCommandType(commandName);

            List<String> commandList = Arrays.asList(commandArray);
            try {
                validator.validateNumberOfArguments(commandName, commandList);
            } catch (InputException e) {
                writeLine(e.getMessage());
                continue;
            }

            LOG_MANAGER.debug("HashMap для аргументов была создана.");
            Map<String, String> arguments = new HashMap<>();
            if (commandType.equals(CommandType.COMPOUND_COMMAND)) {
                arguments = getArgumentsOfCompoundCommands(commandName);
            }

            QueryBuilder queryBuilder = queryBuilderFactory.getQueryBuilder(commandType);
            Query query = null;
            try {
                query = queryBuilder.buildQuery(commandName,
                        commandList,
                        arguments);
            } catch (InputException e) {
                writeLine(e.getMessage());
                continue;
            }

            try {
                Response response = controller.handleQuery(query);
                writeLine(response.getAnswer() + System.lineSeparator());

                if (response.getStatus().getCode().equals("601")) {
                    System.exit(0);
                }
            } catch (CreationException e) {
                throw new InputException(e.getMessage());
            }
        }
    }

    public void write(String text) throws InputException {
        try {
            byte[] buffer = text.getBytes();
            writer.write(buffer, 0, buffer.length);
            writer.flush();
        } catch (IOException e) {
            throw new InputException(INTERNAL_ERROR_WITH_IO);
        }
    }

    public void writeLine(String string) throws InputException {
        String output = string + System.lineSeparator();

        write(output);
    }

    public void writeLine() throws InputException {
        write(System.lineSeparator());
    }

    /**
     * Reads next line from BufferedReader.
     * Note: if user writes empty line then this method return null.
     *
     * @return user input. If it's empty then return null.
     * @throws InputException
     */
    public String readLine() throws InputException {
        write(System.lineSeparator() + ">");

        try {
            String userInput = reader.readLine();

            if (userInput != null) {
                userInput = userInput.trim();

                Pattern pattern = Pattern.compile("^[ +]");
                Matcher matcher = pattern.matcher(userInput);

                if (userInput.isEmpty() || matcher.find()) {
                    userInput = null;
                }
            }


            return userInput;
        } catch (IOException exception) {
            throw new InputException(exception.getMessage());
        }
    }

    /**
     * This method gets map of fields and invitation messages for user's input, display the message,
     * reads user's input and validate each field's value until user's input is correct.
     * Returns the map of field names and argument values.
     *
     * @param name
     * @return
     * @throws InputException
     */
    public Map<String, String> getArgumentsOfCompoundCommands(CommandName name) throws InputException {
        Map<String, String> mapOfArguments = new HashMap<>();
        Map<String, String> mapForInputArguments = interpretator.getMapForInputArguments(name, viewer);

        for (Map.Entry<String, String> entry : mapForInputArguments.entrySet()) {
            String field = entry.getKey();
            String message = entry.getValue();
            write(message);
            Boolean flag = true;
            String correctValue = null;

            while (flag) {
                try {
                    String userInput = readLine();
                    if (userInput != null) userInput = userInput.trim();
                    LOG_MANAGER.info("Введено значение полля.");
                    validator.validateElementFields(field, userInput);
                    if (userInput == null) {
                        LOG_MANAGER.warn("Поле со значением null.");
                        writeLine("Введен null");
                    }
                    flag = false;
                    correctValue = userInput;
                } catch (InputException e) {
                    LOG_MANAGER.errorThrowable("Введено некоректное значение.", e);
                    writeLine(e.getMessage());
                    flag = true;
                }
            }
            mapOfArguments.put(field, correctValue);
            if (field.equals("groupAdminName")) break;

        }
        return mapOfArguments;
    }
}
