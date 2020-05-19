package app;

import app.Exceptions.InputException;
import app.query.CommandName;
import app.query.CommandType;
import app.query.queryBuilder.QueryBuilderFactory;
import connection.Connection;
import connection.SocketConnection;
import connection.exception.ConnectionException;
import connectionWorker.ConnectionWorker;
import manager.LogManager;
import message.EntityType;
import message.Message;
import message.exception.WrongTypeException;
import query.QueryDTO;
import response.Response;
import serializer.exception.DeserializationException;
import serializer.exception.SerializationException;

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
    private final QueryBuilderFactory queryBuilderFactory;

    private static final LogManager LOG_MANAGER = LogManager.createDefault(Console.class);

    public Console(InputStream input,
                   OutputStream output) {
        this.reader = new BufferedReader(new InputStreamReader(input));
        this.writer = new BufferedOutputStream(output);
        this.interpretator = new Interpretator();
        this.validator = new Validator();
        this.viewer = new Viewer();
        this.queryBuilderFactory = new QueryBuilderFactory(validator, interpretator);
    }

    public void start() throws InputException {
        writeLine(START_MESSAGE_STRING);

        while (true) {
            writeLine(viewer.showInvitationCommandMessage());

            LOG_MANAGER.info("Input of commands...");
            String command = readLine();

            if (command == null) {
                LOG_MANAGER.warn("An empty string was entered.");
                writeLine(viewer.showEnteredNullMessage());
                continue;
            }

            String[] commandArray = command.split("[\\s]+");

            try {
                validator.validateCommandName(commandArray[0]);
            } catch (InputException e) {
                LOG_MANAGER.error("An error occurred entering the command...");
                writeLine(e.getMessage());
                continue;
            }

            CommandName commandName = CommandName.getCommandNameEnum(commandArray[0]);
            CommandType commandType = interpretator.interpretateCommandType(commandName);
            LOG_MANAGER.info("Entered the command " + commandName);

            List<String> commandList = Arrays.asList(commandArray);
            try {
                validator.validateNumberOfArguments(commandName, commandList);
            } catch (InputException e) {
                writeLine(e.getMessage());
                continue;
            }

            Map<String, String> arguments = new HashMap<>();
            if (commandType.equals(CommandType.COMPOUND_COMMAND)) {
                arguments = getArgumentsOfCompoundCommands(commandName);
            }

            LOG_MANAGER.info("With arguments " + arguments);

//            QueryBuilder queryBuilder = queryBuilderFactory.getQueryBuilder(commandType);
//            try {
//                Query query = queryBuilder.buildQuery(commandName,
//                        commandList,
//                        arguments);
//            } catch (InputException e) {
//                writeLine(e.getMessage());
//                continue;
//            }

            try {
                Connection connection = new SocketConnection("localhost", 8010, 128);
                ConnectionWorker connectionWorker = ConnectionWorker.createDefault(connection);

                connectionWorker.connect();
                LOG_MANAGER.debug("The connection was SUCCESSFUL.");

                QueryDTO queryDTO = new QueryDTO();
                queryDTO.commandName = commandName.getName();
                queryDTO.arguments = arguments;

                Message query = new Message(EntityType.COMMAND_QUERY, queryDTO);
                connectionWorker.send(query);
                LOG_MANAGER.info("The message is sent: " + queryDTO.toString()  );

                Message receivedMessage = connectionWorker.read();
                Response response = receivedMessage.getResponse();
                LOG_MANAGER.info("The responce is received: " + response.toString());
                write(response.getAnswer());

                if (response.getStatus().getCode().equals("601")) {
                    System.exit(0);
                    LOG_MANAGER.info("Client left the server. ");
                }
            } catch (ConnectionException | SerializationException | DeserializationException | WrongTypeException e) {
                LOG_MANAGER.errorThrowable("Communication error ", e);
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
                    LOG_MANAGER.info("Введено значение поля.");
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
        }
        return mapOfArguments;
    }
}
