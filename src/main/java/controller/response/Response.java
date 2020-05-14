package controller.response;

/**
 * This class represents the response to the executable command.
 * It has a response to the executed command, and a response code.
 */
public final class Response {
    private final Status status;
    private final String answer;

    public Response(Status status, String answer) {
        this.status = status;
        this.answer = answer;
    }

    public Status getStatus() {
        return status;
    }

    public String getAnswer() {
        return answer;
    }

    public static Response getResponse(ResponseDTO responseDTO){
        Status returnedStatus = Status.getStatusEnum(String.valueOf(responseDTO.status));

        return new Response(returnedStatus,
                responseDTO.answer);
    }

    public static ResponseDTO getResponseDTO(Response response){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.answer = response.getAnswer();
        responseDTO.status = response.getStatus().getCode();

        return responseDTO;
    }
}
