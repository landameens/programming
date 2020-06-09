package server.middleware;

import controller.migration.Interpretator;
import manager.LogManager;
import middleware.Middleware;
import middleware.MiddlewareException;
import query.Query;
import response.Response;
import response.ResponseDTO;
import response.Status;

public final class CommandNameValidator extends Middleware {
    private static final LogManager LOG_MANAGER = LogManager.createDefault(CommandNameValidator.class);


    private final Interpretator interpretator;


    public CommandNameValidator(Interpretator interpretator) {
        this.interpretator = interpretator;
    }


    @Override
    public Response handle(Query query) throws MiddlewareException {
        if (query.getCommandName() == null || !interpretator.isSuchCommandExists(query.getCommandName())) {
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.status = Status.BAD_REQUEST.getStringResult();
            responseDTO.answer = "No such command.";

            return Response.createResponse(responseDTO);
        }

        if (query.getArguments() == null) {
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.status = Status.BAD_REQUEST.getStringResult();
            responseDTO.answer = "No arguments.";

            return Response.createResponse(responseDTO);
        }

        if (interpretator.isLoginControllerCommand(query.getCommandName())) {
            LOG_MANAGER.info("Redirect on login");
            return callLeave(query, "login");
        }

        LOG_MANAGER.info("Redirect on old");
        return callLeave(query, "old");
    }
}
