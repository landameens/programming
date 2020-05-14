package app.query;

/**
 * This enum contains all command names
 */
public enum CommandName {
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    SAVE("save"),
    EXECUTE_SCRIPT("execute_script"),
    EXIT("exit"),
    ADD_IF_MIN("add_if_min"),
    REMOVE_LOWER("remove_lower"),
    HISTORY("history"),
    COUNT_BY_GROUP_ADMIN("count_by_group_admin"),
    FILTER_BY_SHOULD_BE_EXPELLED("filter_by_should_be_expelled"),
    FILTER_LESS_THEN_SHOULD_BE_EXPELLED("filter_less_than_should_be_expelled");

    private final String name;

    CommandName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static CommandName getCommandNameEnum(String name){
        CommandName[] commandNames = CommandName.values();

        for (CommandName commandName : commandNames){
            if(name.equals(commandName.getName())){
                return commandName;
            }
        }

        return null;
    }

}
