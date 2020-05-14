package controller.commands;

import app.Viewer;
import app.query.CommandName;
import app.query.CommandType;
import controller.commands.factory.*;
import controller.commands.scripts.RecursionChecker;
import domain.commandsRepository.ICommandsRepository;
import domain.studyGroupRepository.IStudyGroupRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.query.CommandName.*;
import static app.query.CommandType.*;
import static app.query.CommandType.SIMPLE_COMMAND;

/**
 * Class for an assign to a command to each factory
 */
public class Interpretator {
    private final ICommandFactory simpleCommandsFactory;
    private final ICommandFactory studyGroupRepositoryCommandFactory;
    private final ICommandFactory commandRepositoryFactory;
    private final ICommandFactory scriptFactory;

    private final Map<String, Class<? extends ICommandFactory>> commandFactoryMap = new HashMap<String, Class<? extends ICommandFactory>>(){
        {
            put("help", SimpleCommandsFactory.class);
            put("show", StudyGroupRepositoryCommandFactory.class);
            put("add", StudyGroupRepositoryCommandFactory.class);
            put("remove_by_id", StudyGroupRepositoryCommandFactory.class);
            put("clear", StudyGroupRepositoryCommandFactory.class);
            put("update", StudyGroupRepositoryCommandFactory.class);
            put("add_if_min", StudyGroupRepositoryCommandFactory.class);
            put("save", StudyGroupRepositoryCommandFactory.class);
            put("remove_lower", StudyGroupRepositoryCommandFactory.class);
            put("exit", SimpleCommandsFactory.class);
            put("filter_by_should_be_expelled", StudyGroupRepositoryCommandFactory.class);
            put("filter_less_than_should_be_expelled", StudyGroupRepositoryCommandFactory.class);
            put("count_by_group_admin", StudyGroupRepositoryCommandFactory.class);
            put("info", StudyGroupRepositoryCommandFactory.class);
            put("execute_script", ScriptCommandFactory.class);
            put("history", HistoryRepositoryCommandFactory.class);
        }
    };

    private Map<CommandName, CommandType> allCommands = new HashMap<CommandName, CommandType>(){
        {
            put(HELP, COMMAND_WITHOUT_ARGUMENTS);
            put(INFO, COMMAND_WITHOUT_ARGUMENTS);
            put(SHOW, COMMAND_WITHOUT_ARGUMENTS);
            put(ADD, COMPOUND_COMMAND);
            put(UPDATE, COMPOUND_COMMAND);
            put(REMOVE_BY_ID, SIMPLE_COMMAND);
            put(CLEAR, COMMAND_WITHOUT_ARGUMENTS);
            put(SAVE, COMMAND_WITHOUT_ARGUMENTS);
            put(EXECUTE_SCRIPT, SIMPLE_COMMAND);
            put(EXIT, COMMAND_WITHOUT_ARGUMENTS);
            put(ADD_IF_MIN, COMPOUND_COMMAND);
            put(REMOVE_LOWER, COMPOUND_COMMAND);
            put(COUNT_BY_GROUP_ADMIN, COMPOUND_COMMAND);
            put(HISTORY, COMMAND_WITHOUT_ARGUMENTS);
            put(FILTER_BY_SHOULD_BE_EXPELLED, SIMPLE_COMMAND);
            put(FILTER_LESS_THEN_SHOULD_BE_EXPELLED, SIMPLE_COMMAND);
        }

    };

    private Map<CommandName,String> mapOfNamesAndFields = new HashMap<CommandName, String>(){
        {
            put(REMOVE_BY_ID, "id");
            put(EXECUTE_SCRIPT, "file_name");
            put(FILTER_BY_SHOULD_BE_EXPELLED, "should_be_expelled");
            put(FILTER_LESS_THEN_SHOULD_BE_EXPELLED, "should_be_expelled");

            put(UPDATE, "id");
        }
    };

    public Interpretator(IStudyGroupRepository studyGroupRepository,
                         ICommandsRepository historyRepository,
                         RecursionChecker recursionChecker) {
        simpleCommandsFactory = new SimpleCommandsFactory();
        studyGroupRepositoryCommandFactory = new StudyGroupRepositoryCommandFactory(studyGroupRepository);
        commandRepositoryFactory = new HistoryRepositoryCommandFactory(historyRepository);
        scriptFactory = new ScriptCommandFactory(studyGroupRepository, historyRepository, recursionChecker);
    }

    /**
     * Method to get an instance of the factory for the name of the command
     * @param name
     * @return factory corresponding to the command
     */
    public ICommandFactory getFactoryInstance(String name){
        Class<? extends ICommandFactory> clazz = commandFactoryMap.get(name);

        if (clazz.equals(simpleCommandsFactory.getClass())) {
            return simpleCommandsFactory;
        }

        if (clazz.equals(studyGroupRepositoryCommandFactory.getClass())) {
            return studyGroupRepositoryCommandFactory;
        }

        if(clazz.equals(commandRepositoryFactory.getClass())){
            return commandRepositoryFactory;
        }

        if(clazz.equals(scriptFactory.getClass())){
            return scriptFactory;
        }

        return null;
    }


    public CommandType interpretateCommandType(CommandName commandName){
        return allCommands.get(commandName);
    }

    /**
     * Returns a map of fields and invitation messages for a command.
     * @param name
     * @param viewer
     * @return
     */
    public Map<String, String> getMapForInputArguments (CommandName name, Viewer viewer){
        return viewer.getInputMessagesMap().get(name);
    }

    /**
     * Makes map of field and argument values for building a query for simple commands.
     * @param name
     * @param commandList
     * @return
     */
    public Map<String, String> interpretateSimpleCommandArguments (CommandName name,
                                                                   List<String> commandList) {
        String field = mapOfNamesAndFields.get(name);
        String argument = commandList.get(1);

        Map<String, String> map = new HashMap<>();
        map.put(field, argument);

        return map;
    }
}
