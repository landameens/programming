package app.query;

import java.util.Map;

/**
 * This class contains all the information about the command, its name, type and arguments.
 */
public final class Query {
    private String commandName;
    private Map<String, String> arguments;

    public Query(String commandName,
                 Map<String,String> arguments){
        this.commandName = commandName;
        this.arguments = arguments;
    }

    public String getCommandName() {
        return commandName;
    }

    public Map<String, String> getArguments() {
        return arguments;
    }

    //for testing
    @Override
    public String toString(){
        return "Query: commandName = "+ commandName +", arguments = " + arguments.toString();
    }
}

