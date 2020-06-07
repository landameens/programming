package server.view;

public class Viewer {
    public String showGoodbyeMessage() {
        return "Stopping server." + System.lineSeparator() + "See ypu later";
    }

    public String showNullEnterInput() {
        return "Entered NULL.";
    }

    public String showOfferToRepeatInput() {
        return "Please repeat input.";
    }

    public String showNoSuchCommandErrorMessage() {
        return "ERROR: no such command";
    }

    public String showInternalServerErrorMessage() {
        return "ERROR: internal server error";
    }

    public String showBadRequestErrorMessage() {
        return "ERROR: the request is bad.";
    }

    public String showControllerPrefixAnswer() {
        return "Controller returned: ";
    }
}
