package controller.response;

/**
 * All response codes of the executed command.
 */
public enum Status {
    SUCCESSFULLY("200"),
    BAD_REQUEST("400"),
    INTERNAL_ERROR("500"),
    PRECONDITION_FAILED("412"),
    PROGRAMM_EXIT("601");

    private String code;

    Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Status getStatusEnum(String code){
        Status[] allStatuses = Status.values();

        for (Status status : allStatuses) {
            if (code.equals(status.getCode())) {
                return status;
            }
        }

        return null;
    }
}
