package controller.getController;

import controller.command.Command;
import controller.command.exception.CommandExecutionException;
import controller.services.JSONAdapter;
import domain.user.User;
import domain.userRepository.UserRepository;
import domain.userRepository.exception.UserRepositoryException;
import domain.userRepository.getQuery.GetAllQuery;
import domain.userRepository.getQuery.GetQuery;
import org.apache.commons.configuration2.Configuration;
import response.Response;
import response.Status;

import java.util.List;
import java.util.Map;

public final class GetAllUsersCommand extends Command {
    private UserRepository userRepository;
    private JSONAdapter jsonAdapter;

    /**
     * Use for creating command via factories.
     */
    public GetAllUsersCommand(String commandName, Map<String, String> arguments, Configuration configuration) {
        super(commandName, arguments, configuration);
    }

    @Override
    protected Response processExecution() throws CommandExecutionException {
        GetQuery getQuery = new GetAllQuery();

        List<User> users;
        try {
            users = userRepository.get(getQuery);
        } catch (UserRepositoryException e) {
            throw new CommandExecutionException(e);
        }

        return new Response(Status.SUCCESSFULLY, jsonAdapter.toJson(users));
    }
}
