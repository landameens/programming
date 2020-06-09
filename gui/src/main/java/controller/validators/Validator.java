package controller.validators;

public abstract class Validator {
    private Validator nextValidator;

    public Validator linkWith(Validator nextValidator) {
        this.nextValidator = nextValidator;
        return nextValidator;
    }

    public abstract String validateFields(String login, String password, String repeatedPassword);

    protected String validateNext(String login, String password, String repeatedPassword) {
        if (nextValidator == null) {
            return "";
        }

        return nextValidator.validateFields(login, password, repeatedPassword);
    }
}
