package controller.commands.factory;

import controller.commands.Command;
import controller.commands.history.HistoryCommand;
import domain.commandsRepository.ICommandsRepository;
import domain.exception.CreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * factory for History Command
 */
public class HistoryRepositoryCommandFactory implements ICommandFactory {
    private ICommandsRepository commandsRepository;

    public HistoryRepositoryCommandFactory(ICommandsRepository commandsRepository) {
        this.commandsRepository = commandsRepository;
    }

    private Map<String, Class<? extends Command>> classMap = new HashMap<String, Class<? extends Command>>() {
        {
            put("history", HistoryCommand.class);
        }
    };

    /**
     * Method for creating a command with instances of a group repository, and a repository with executed commands using reflection
     * @param commandName
     * @param arguments
     * @return Command
     * @throws CreationException
     */
    @Override
    public Command createCommand(String commandName,
                                 Map<String, String> arguments) throws CreationException {
        Class<? extends Command> clazz = classMap.get(commandName);

        try {
            Constructor<? extends Command> constructor = clazz.getConstructor(String.class, Map.class, ICommandsRepository.class);
            return constructor.newInstance(commandName, arguments, commandsRepository);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new CreationException(e);
        }
    }
}
