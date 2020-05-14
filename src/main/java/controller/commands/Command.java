package controller.commands;

import controller.response.Response;
import controller.response.ResponseDTO;
import controller.response.Status;

import java.util.Map;

/**
 * Abstract class for all commands
 */
public abstract class Command {
    protected String name;
    protected Map<String, String> args;
    protected ResponseDTO responseDTO;

    public Command(String name,
                   Map<String, String> args) {
        this.name = name;
        this.args = args;
        responseDTO = new ResponseDTO();
    }

    /**
     * Method for creating a ready response with code 200
     * @param message
     * @return ready response
     */
    protected Response getSuccessfullyResponseDTO(String message){
        responseDTO.status = Status.SUCCESSFULLY.getCode();
        responseDTO.answer = message;

        return Response.getResponse(responseDTO);
    }

    /**
     * Method for creating a ready response with code 400
     * @param message
     * @return ready response
     */
    protected Response getBadRequestResponseDTO(String message){
        responseDTO.status = Status.BAD_REQUEST.getCode();
        responseDTO.answer = message;

        return Response.getResponse(responseDTO);
    }

    /**
     * Method for creating a ready response with code 500
     * @param message
     * @return ready response
     */
    protected Response getInternalErrorResponseDTO(String message){
        responseDTO.status = Status.INTERNAL_ERROR.getCode();
        responseDTO.answer = message;

        return Response.getResponse(responseDTO);
    }

    /**
     * Method for creating a ready response with code 412
     * @param message
     * @return ready response
     */
    protected Response getPreconditionFailedResponseDTO(String message){
        responseDTO.status = Status.PRECONDITION_FAILED.getCode();
        responseDTO.answer = message;

        return Response.getResponse(responseDTO);
    }

    /**
     * Method for creating a ready response with code 601
     * @param message
     * @return ready response
     */
    protected Response getProgrammExitResponceDTO(String message){
        responseDTO.status = Status.PROGRAMM_EXIT.getCode();
        responseDTO.answer = message;

        return Response.getResponse(responseDTO);
    }

    /**
     * Abstract method for executing the command.
     * The implementation is different for each command.
     * @return response
     */
    public abstract Response execute();
}
