package app.query.queryBuilder;

import app.query.CommandName;
import app.Interpretator;
import app.Validator;
import app.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for making query of commands that has no arguments.
 */
public class QueryBuilderForCommandsWithoutArguments extends QueryBuilder {

    public QueryBuilderForCommandsWithoutArguments(Validator validator, Interpretator interpretator) {
        super(validator, interpretator);
    }

    /**
     * Makes query for commands without arguments by command name and command type.
     * @param name
     * @param commandList
     * @param arguments
     * @return
     */
    @Override
    public Query buildQuery(CommandName name,
                            List<String> commandList,
                            Map<String, String> arguments) {
        arguments = new HashMap<>();
        return new Query(name.getName(), arguments );
    }
}
