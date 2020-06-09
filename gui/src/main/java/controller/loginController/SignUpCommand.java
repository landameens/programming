package controller.loginController;

import controller.command.Command;
import controller.command.exception.CommandExecutionException;
import controller.serverAdapter.ServerAdapter;
import controller.serverAdapter.exception.ServerAdapterException;
import org.apache.commons.configuration2.Configuration;
import query.Query;
import response.Response;
import response.Status;

import java.util.HashMap;
import java.util.Map;

public final class SignUpCommand extends Command {
    private ServerAdapter serverAdapter;


    /**
     * Use for creating command via factories.
     */
    public SignUpCommand(String commandName, Map<String, String> arguments, Configuration configuration) {
        super(commandName, arguments, configuration);
    }

    @Override
    protected Response processExecution() throws CommandExecutionException {
        String userInput = arguments.get("userInput");
        String[] subStrings = userInput.split(" +");

        if (subStrings.length != 3) {
            return new Response(Status.BAD_REQUEST, "SignUp command format: \"sign_up LOGIN PASSWORD\"");
        }

        String login = subStrings[1];
        String password = subStrings[2];

        Map<String, String> queryArguments = new HashMap<>();
        queryArguments.put("login", login);
        queryArguments.put("password", password);

        Query query = new Query("signUp", queryArguments);

        try {
            return serverAdapter.send(query);
        } catch (ServerAdapterException e) {
            throw new CommandExecutionException(e);
        }
    }
}
