package controller.commands.factory;

import controller.commands.Command;
import controller.commands.scripts.ExecuteScriptCommand;
import controller.commands.scripts.RecursionChecker;
import domain.commandsRepository.ICommandsRepository;
import domain.exception.CreationException;
import domain.studyGroupRepository.IStudyGroupRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for scriptCommand.
 */
public class ScriptCommandFactory implements ICommandFactory {
    private final IStudyGroupRepository studyGroupRepository;
    private final ICommandsRepository commandsRepository;
    private final RecursionChecker recursionChecker;

    public ScriptCommandFactory(IStudyGroupRepository studyGroupRepository, ICommandsRepository commandsRepository, RecursionChecker recursionChecker) {
        this.commandsRepository = commandsRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.recursionChecker = recursionChecker;
    }

    private final Map<String, Class<? extends Command>> classMap = new HashMap<String, Class<? extends Command>>() {
        {
            put("execute_script", ExecuteScriptCommand.class);
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
            Constructor<? extends Command> constructor = clazz.getConstructor(String.class, Map.class, IStudyGroupRepository.class, ICommandsRepository.class, RecursionChecker.class);
            return constructor.newInstance(commandName, arguments, studyGroupRepository, commandsRepository, recursionChecker);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new CreationException(e);
        }
    }
}
