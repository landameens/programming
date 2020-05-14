package app.query.queryBuilder;

import app.query.CommandName;
import app.Exceptions.InputException;
import app.Interpretator;
import app.Validator;
import app.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for making query of commands that has compound arguments.
 */
public class QueryBuilderForCompoundCommands extends QueryBuilder {

    public QueryBuilderForCompoundCommands(Validator validator, Interpretator interpretator) {
        super(validator, interpretator);
    }

    /**
     * Makes query for compound commands by command name, command type,
     * commandList that contains simple arguments and hashMap of complex arguments.
     * @param name
     * @param commandList
     * @param arguments
     * @return
     * @throws InputException
     */
    @Override
    public Query buildQuery(CommandName name,
                            List<String> commandList,
                            Map<String,String> arguments) throws InputException {
        validator.validateSimpleArgumentsOfCompoundCommand(name, commandList);

        Map<String, String> simpleArguments = new HashMap<>();
        if (name.equals(CommandName.UPDATE)) {
            simpleArguments = interpretator.interpretateSimpleCommandArguments(name, commandList);
        }
        arguments.putAll(simpleArguments);

        return new Query(name.getName(), arguments);
    }
}
