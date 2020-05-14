package app;

import app.query.CommandName;
import app.query.CommandType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.query.CommandName.*;
import static app.query.CommandType.*;

/**
 * This class is responsible for interpretating user's input
 */
public final class Interpretator {

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
