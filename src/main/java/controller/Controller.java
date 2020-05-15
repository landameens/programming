package controller;

import query.Query;
import controller.commands.Command;
import controller.commands.factory.ICommandFactory;
import controller.commands.scripts.ExecuteScriptCommand;
import controller.commands.scripts.RecursionChecker;
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
        Command command = commandFactory.createCommand(query.getCommandName(), query.getArguments());

        addRecordToHistory(query);
        LOG_MANAGER.debug("Команда добавлена в историю.");

        LOG_MANAGER.debug("Выполнение команды...");
        Response response = command.execute();

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