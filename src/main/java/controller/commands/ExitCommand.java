package controller.commands;

import response.Response;
import manager.LogManager;

import java.util.Map;

public class ExitCommand extends Command {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(ExitCommand.class);
    public ExitCommand(String name, Map<String, String> args) {
        super(name, args);
    }

    @Override
    public Response execute() {
        LOG_MANAGER.info("Осуществляется выход из программы.");
        return  getProgrammExitResponceDTO("Произведен выход из программы.");
    }
}
