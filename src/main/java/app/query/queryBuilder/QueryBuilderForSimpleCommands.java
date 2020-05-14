package app.query.queryBuilder;

import app.query.CommandName;
import app.Exceptions.InputException;
import app.Interpretator;
import app.Validator;
import app.query.Query;

import java.util.List;
import java.util.Map;

/**
 * This class is responsible for making query of commands that has simple arguments.
 */
public class QueryBuilderForSimpleCommands extends QueryBuilder {

    public QueryBuilderForSimpleCommands(Validator validator, Interpretator interpretator) {
        super(validator, interpretator);
    }

    /**
     * Makes query for simple commands by command name, command type and commandList that contains simple arguments.
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
        validator.validateSimpleCommandArguments(name, commandList);
        arguments = interpretator.interpretateSimpleCommandArguments(name, commandList);
        return new Query(name.getName(), arguments);
    }
}
