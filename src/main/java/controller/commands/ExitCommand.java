package controller.commands;

import controller.response.Response;

import java.util.Map;

public class ExitCommand extends Command {
    public ExitCommand(String name, Map<String, String> args) {
        super(name, args);
    }

    @Override
    public Response execute() {
        return  getProgrammExitResponceDTO("Произведен выход из программы.");
    }
}
