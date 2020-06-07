package controller.loginController;

import controller.command.Command;
import controller.command.exception.CommandExecutionException;
import controller.services.PasswordHashService;
import domain.user.User;
import domain.userRepository.UserRepository;
import domain.userRepository.exception.UserRepositoryException;
import domain.userRepository.getQuery.GetQuery;
import domain.userRepository.getQuery.GetQueryWithSpecialField;
import org.apache.commons.configuration2.Configuration;
import response.Response;
import response.Status;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public final class LoginCommand extends Command {
    private UserRepository userRepository;
    private PasswordHashService passwordHashService;


    /**
     * Use for creating command via factories.
     */
    public LoginCommand(String commandName, Map<String, String> arguments, Configuration configuration) {
        super(commandName, arguments, configuration);
    }

    @Override
    protected Response processExecution() throws CommandExecutionException {
        String login = arguments.get("login");
        String password = arguments.get("password");

        try {
            password = passwordHashService.hash(password);
        } catch (NoSuchAlgorithmException e) {
            throw new CommandExecutionException(e);
        }

        GetQuery getQuery = new GetQueryWithSpecialField(User.class, "login", login);
        List<User> users;
        try {
            users = userRepository.get(getQuery);
        } catch (UserRepositoryException e) {
            throw new CommandExecutionException(e);
        }

        if (users.isEmpty()) {
            return new Response(Status.BAD_REQUEST, "No user with this login");
        }

        if (password.equals(users.iterator().next().getPassword())) {
            return new Response(Status.SUCCESSFULLY, login + " " + password);
        }

        return new Response(Status.BAD_REQUEST, "Wrong password");
    }
}
