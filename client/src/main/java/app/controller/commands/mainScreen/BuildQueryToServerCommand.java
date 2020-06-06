package client.controller.commands.mainScreen;

import adapter.LoggerAdapter;
import client.controller.services.connectionService.ConnectionService;
import client.controller.services.queryBuilder.QueryBuilder;
import client.controller.services.queryBuilder.mediator.QueryBuilderMediator;
import client.controller.services.queryBuilder.queryCreationException.QueryCreationException;
import client.view.Console;
import controller.command.Command;
import controller.command.exception.CommandExecutionException;
import org.apache.commons.configuration2.Configuration;
import query.Query;
import response.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class BuildQueryToServerCommand extends Command {
    private QueryBuilderMediator queryBuilderMediator;
    private ConnectionService connectionService;
    private Console console;

    private final LoggerAdapter loggerAdapter;

    /**
     * Use for creating command via factories.
     */
    public BuildQueryToServerCommand(String commandName,
                                     Map<String, String> arguments,
                                     Configuration configuration) {
        super(commandName, arguments, configuration);
        loggerAdapter = LoggerAdapter.createDefault(BuildQueryToServerCommand.class.getSimpleName());
    }

    @Override
    protected Response processExecution() throws CommandExecutionException {
        String userInput = arguments.get("userInput");
        Query query;
        try {
            String[] subStrings = getSubStrings(userInput);
            String commandName = subStrings[0];

            loggerAdapter.info("User entered command: " + commandName + ".");

            QueryBuilder queryBuilder = queryBuilderMediator.get(commandName);

            List<String> inputArguments = Arrays.asList(Arrays.copyOfRange(subStrings, 1, subStrings.length));

            String accessToken = arguments.get("accessToken");

            if (accessToken == null) {
                accessToken = "";
            }

            query = queryBuilder.create(inputArguments,
                                        console,
                                        accessToken);

            loggerAdapter.info("Query was created SUCCESSFULLY.");
        } catch (QueryCreationException exception) {
            throw new CommandExecutionException(exception);
        }

        return connectionService.send(query);
    }

    private String[] getSubStrings(String userInput) {
        return userInput.split(" +");
    }
}
