package client.controller.commands;

import client.controller.services.exitingDirector.ExitingDirector;
import controller.command.Command;
import org.apache.commons.configuration2.Configuration;
import response.Response;
import response.Status;

import java.util.Map;

public final class ExitCommand extends Command {
    private ExitingDirector exitingDirector;

    /**
     * Use for creating command via factories.
     *
     */
    public ExitCommand(String commandName,
                       Map<String, String> arguments,
                       Configuration configuration) {
        super(commandName, arguments, configuration);
    }

    @Override
    protected Response processExecution() {
        exitingDirector.exit();
        return new Response(Status.SUCCESSFULLY, "");
    }
}
