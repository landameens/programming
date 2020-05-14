package controller.commands.factory;

import controller.commands.Command;
import controller.commands.studyGroupRep.*;
import domain.exception.CreationException;
import domain.studyGroupRepository.IStudyGroupRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for commands which uses study Group repository.
 */
public class StudyGroupRepositoryCommandFactory implements ICommandFactory {
    private IStudyGroupRepository studyGroupRepository;

    public StudyGroupRepositoryCommandFactory(IStudyGroupRepository studyGroupRepository) {
        this.studyGroupRepository = studyGroupRepository;
    }


    private Map<String, Class<? extends Command>> classMap = new HashMap<String, Class<? extends Command>>() {
        {
            put("show", ShowCommand.class);
            put("add", AddCommand.class);
            put("remove_by_id", RemoveByIdCommand.class);
            put("clear", ClearCommand.class);
            put("update", UpdateCommad.class);
            put("add_if_min", AddIfMinCommand.class);
            put("save", SaveCommand.class);
            put("remove_lower", RemoveLowerCommand.class);
            put("filter_by_should_be_expelled", FilterByShouldBeExpelledCommand.class);
            put("filter_less_than_should_be_expelled", FilterLessThanShouldBeExpelledCommand.class);
            put("count_by_group_admin", CountByGroupAdminCommand.class);
            put("info", InfoCommand.class);
        }
    };


    @Override
    public Command createCommand(String commandName,
                                 Map<String, String> arguments) throws CreationException {
        Class<? extends Command> clazz = classMap.get(commandName);

        try {
            Constructor<? extends Command> constructor = clazz.getConstructor(String.class, Map.class, IStudyGroupRepository.class);
            return constructor.newInstance(commandName, arguments, studyGroupRepository);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new CreationException(e);
        }
    }
}
