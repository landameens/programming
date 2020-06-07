package controller.migration;

import query.Query;
import controller.migration.commands.Command;
import controller.migration.commands.factory.ICommandFactory;
import controller.migration.commands.scripts.ExecuteScriptCommand;
import controller.migration.commands.scripts.RecursionChecker;
import response.Response;
import domain.commandsRepository.ICommandsRepository;
import domain.commandsRepository.Record;
import domain.exception.CreationException;
import manager.LogManager;

/**
 * Class for processing user requests
 */
public final class Controller {
    private final Interpretator interpretator;
    private final ICommandsRepository commandsRepository;

    private static final LogManager LOG_MANAGER = LogManager.createDefault(Controller.class);


    public Controller(Interpretator interpretator,
                      ICommandsRepository commandsRepository) {
        this.interpretator = interpretator;
        this.commandsRepository = commandsRepository;
    }

    /**
     * Method for creating and executing a user's command
     *
     * @param query
     * @return response to the command
     * @throws CreationException
     */
    public Response handleQuery(Query query) throws CreationException {
        ICommandFactory commandFactory = interpretator.getFactoryInstance(query.getCommandName());
        LOG_MANAGER.debug("CommandFactory " + commandFactory.getClass().getSimpleName() + " was created SUCCESFUL");
        Command command = commandFactory.createCommand(query.getCommandName(), query.getArguments());
        LOG_MANAGER.debug("Command " + command.getClass().getSimpleName() + " was created SUCCESSFUL");

        addRecordToHistory(query);
        LOG_MANAGER.debug("The command is added to the history.");

        Response response = command.execute();
        LOG_MANAGER.debug("Received responce: " + response.toString());

        if (command.getClass().equals(ExecuteScriptCommand.class)) {
            RecursionChecker.cleanRecursionChecker();
        }

        return response;
    }

    private void addRecordToHistory(Query query) {
        Record recordDTO = new Record();
        recordDTO.name = query.getCommandName();
        commandsRepository.add(recordDTO);
    }
}