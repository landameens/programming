package app.query;

/**
 * This enum contains types of commands.
 * A simple command is a command that has only non-composite arguments, that user writes in the same line as a command name.
 * A compound command is a command what has compound arguments (it consists of many fields)
 */
public enum CommandType {
    COMMAND_WITHOUT_ARGUMENTS("CommandWithoutArguments"),
    SIMPLE_COMMAND("SimpleCommand"),
    COMPOUND_COMMAND("CompoundCommand");

    private String name;

    CommandType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CommandType getCommandTypeEnum(String type) {
        CommandType[] commandTypes = CommandType.values();
        for(CommandType commandType : commandTypes){
            if (type.equals(commandType.getName())){
                return commandType;
            }
        }

        return null;
    }
}
